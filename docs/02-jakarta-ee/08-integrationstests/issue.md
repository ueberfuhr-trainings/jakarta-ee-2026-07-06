---
layout: default
title: Integrationstests der HTTP-Schnittstelle
---

# Integrationstests der HTTP-Schnittstelle

Bisher haben wir die Anwendung von Hand im Browser ausprobiert. In dieser Übung
automatisierst Du das: Beim `mvn verify` wird **Open Liberty gestartet**, die
Anwendung deployt und über **HTTP von außen** getestet – die App ist dabei eine
**Blackbox**, Du kennst also nur die HTTP-Schnittstelle, nicht die interne
Implementierung. Als Test-Client kommt **REST-Assured** zum Einsatz. Damit die
Tests reproduzierbar und ohne Seiteneffekte laufen, hängst Du den Server im Test
an eine **In-Memory-Datenbank** statt an die dateibasierte `.localdb`. Das
eigentliche Schreiben vieler Testfälle ist dabei zweitrangig – wichtiger ist,
dass Du das **Konzept** und die **Werkzeugkette** (Maven, Liberty, REST-Assured)
verstehst.

## 🎯 Lernziele

* Du verstehst den Unterschied zwischen einem Blackbox-Integrationstest (Test gegen die HTTP-Schnittstelle der laufenden Anwendung) und einem Unit-Test.
* Du kannst Maven und das `liberty-maven-plugin` so konfigurieren, dass beim `mvn verify` der Server gestartet, die Tests ausgeführt und der Server wieder gestoppt wird.
* Du verstehst, warum für die Tests eine In-Memory-Datenbank sinnvoll ist und wie man sie ohne Änderung der produktiven Konfiguration aktiviert.
* Du kannst mit REST-Assured einen einfachen HTTP-Aufruf absetzen und den Statuscode prüfen.

## ✅ Definition of Done

* [ ] Beim `mvn verify` startet Liberty automatisch, die Integrationstests laufen gegen die laufende Anwendung, danach stoppt Liberty wieder.
* [ ] Die Tests laufen gegen eine In-Memory-H2-Datenbank; die dateibasierte `.localdb` wird dabei **nicht** verwendet und bleibt unverändert.
* [ ] Es existiert mindestens ein Integrationstest (`*IT`), der einen Fehlerfall (Statuscode `400`) prüft.
* [ ] Ihr habt die Reflexionsfragen schriftlich beantwortet.

## 🪜 Arbeitsschritte

### Teil 1: Test-Abhängigkeiten und Build konfigurieren

1. **Test-Dependencies** in die `pom.xml` aufnehmen (Scope `test`):
   ```xml
   <dependency>
       <groupId>org.junit.jupiter</groupId>
       <artifactId>junit-jupiter</artifactId>
       <version>5.10.2</version>
       <scope>test</scope>
   </dependency>
   <dependency>
       <groupId>io.rest-assured</groupId>
       <artifactId>rest-assured</artifactId>
       <version>4.5.1</version>
       <scope>test</scope>
   </dependency>
   ```
2. **Liberty an den Test-Lifecycle binden.** Ergänze das `liberty-maven-plugin` um `<executions>`, sodass der Server erstellt (`create`/`install-feature` in `prepare-package`), zum Testen deployt und gestartet (`deploy`/`start` in `pre-integration-test`) und danach gestoppt wird (`stop` in `post-integration-test`):
   ```xml
   <executions>
       <execution>
           <id>create-server</id>
           <phase>prepare-package</phase>
           <goals>
               <goal>create</goal>
               <goal>install-feature</goal>
           </goals>
       </execution>
       <execution>
           <id>deploy-and-start</id>
           <phase>pre-integration-test</phase>
           <goals>
               <goal>deploy</goal>
               <goal>start</goal>
           </goals>
       </execution>
       <execution>
           <id>stop-server</id>
           <phase>post-integration-test</phase>
           <goals>
               <goal>stop</goal>
           </goals>
       </execution>
   </executions>
   ```
   > `liberty:dev`/`liberty:start` durchlaufen diese Phasen nicht bzw. setzen den Server über `create` bei jedem Start frisch auf – sie verwenden dieselbe `server.xml`, aber ohne den erst später (Schritt 6) eingespielten In-Memory-Override.
3. **Failsafe-Plugin** ergänzen. Anders als das Surefire-Plugin (Unit-Tests, `*Test`) führt das Failsafe-Plugin die Integrationstests (`*IT`) in der Phase `integration-test` aus – also nachdem der Server läuft:
   ```xml
   <plugin>
       <groupId>org.apache.maven.plugins</groupId>
       <artifactId>maven-failsafe-plugin</artifactId>
       <version>3.2.5</version>
       <configuration>
           <systemPropertyVariables>
               <liberty.http.port>9080</liberty.http.port>
           </systemPropertyVariables>
       </configuration>
       <executions>
           <execution>
               <goals>
                   <goal>integration-test</goal>
                   <goal>verify</goal>
               </goals>
           </execution>
       </executions>
   </plugin>
   ```
   > Namenskonvention: `*Test` = Unit-Test (Surefire, Phase `test`), `*IT` = Integrationstest (Failsafe, Phase `integration-test`). So lässt sich beides klar trennen.

### Teil 2: In-Memory-Datenbank nur für die Tests

4. **DB-URL in der `server.xml` als Variable** herausziehen, mit dem bisherigen dateibasierten Wert als Default (produktives Verhalten bleibt unverändert):
   ```xml
   <variable name="todos.db.url"
             defaultValue="jdbc:h2:file:${todos.db.dir}/todos;AUTO_SERVER=TRUE"/>

   <dataSource id="todosDS" jndiName="jdbc/todos">
       <jdbcDriver libraryRef="h2"/>
       <properties URL="${todos.db.url}" user="sa" password="sa"/>
   </dataSource>
   ```
5. **Test-Override anlegen.** Erstelle unter `src/test/liberty/config/configDropins/overrides/inmemory-datasource.xml` eine Datei, die dieselbe Variable auf eine In-Memory-URL setzt:
   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <server>
       <variable name="todos.db.url" value="jdbc:h2:mem:todos;DB_CLOSE_DELAY=-1"/>
   </server>
   ```
   > `jdbc:h2:mem:` hält die DB nur im Speicher. `DB_CLOSE_DELAY=-1` sorgt dafür, dass Schema und Daten für die gesamte JVM-Laufzeit erhalten bleiben – auch wenn der Connection-Pool zwischendurch alle Verbindungen schließt.
6. **Override nur beim Testlauf einspielen.** Lass das `maven-resources-plugin` die Datei in der Phase `pre-integration-test` (nach `create`, vor `liberty:start`) in das Server-Verzeichnis kopieren. Das Plugin muss **vor** dem `liberty-maven-plugin` deklariert sein, damit die Kopie in dieser Phase vor dem Serverstart passiert:
   ```xml
   <plugin>
       <groupId>org.apache.maven.plugins</groupId>
       <artifactId>maven-resources-plugin</artifactId>
       <version>3.3.1</version>
       <executions>
           <execution>
               <id>copy-it-server-config</id>
               <phase>pre-integration-test</phase>
               <goals>
                   <goal>copy-resources</goal>
               </goals>
               <configuration>
                   <outputDirectory>${project.build.directory}/liberty/wlp/usr/servers/defaultServer/configDropins/overrides</outputDirectory>
                   <resources>
                       <resource>
                           <directory>${project.basedir}/src/test/liberty/config/configDropins/overrides</directory>
                       </resource>
                   </resources>
               </configuration>
           </execution>
       </executions>
   </plugin>
   ```
   > Reihenfolge ist entscheidend: `liberty:create` (Phase `prepare-package`) setzt das Server-Verzeichnis frisch auf und würde eine zu früh kopierte Datei wieder entfernen. Deshalb wird der Override erst in `pre-integration-test` – nach `create`, aber vor `start` – eingespielt.
7. **Override nach den Tests wieder entfernen.** Sonst bliebe die Datei im Server-Verzeichnis liegen und ein späterer `liberty:start` (oder ein Start aus der IDE), der kein `create` ausführt, würde sie erben und unbemerkt In-Memory laufen – die Daten in `.localdb` gingen dann beim nächsten Neustart verloren. Lösche sie daher in `post-integration-test` (nach `liberty:stop`, deshalb **nach** dem `liberty-maven-plugin` deklariert):
   ```xml
   <plugin>
       <groupId>org.apache.maven.plugins</groupId>
       <artifactId>maven-clean-plugin</artifactId>
       <version>3.3.2</version>
       <executions>
           <execution>
               <id>remove-it-server-config</id>
               <phase>post-integration-test</phase>
               <goals>
                   <goal>clean</goal>
               </goals>
               <configuration>
                   <excludeDefaultDirectories>true</excludeDefaultDirectories>
                   <filesets>
                       <fileset>
                           <directory>${project.build.directory}/liberty/wlp/usr/servers/defaultServer/configDropins/overrides</directory>
                           <includes>
                               <include>inmemory-datasource.xml</include>
                           </includes>
                       </fileset>
                   </filesets>
               </configuration>
           </execution>
       </executions>
   </plugin>
   ```
   > `excludeDefaultDirectories=true` verhindert, dass das Plugin das komplette `target/` löscht – es entfernt nur die angegebene Datei.

### Teil 3: Einen ersten Integrationstest schreiben

8. **Testklasse anlegen** unter `src/test/java/de/schulung/jakartaee/todos/TodosServletIT.java`. Konfiguriere REST-Assured einmalig (Basis-URL, Port, Kontextpfad) und prüfe einen einfachen **Fehlerfall**: Ein `POST` auf `/add-todo` mit zu kurzem Titel muss mit `400 Bad Request` abgelehnt werden.
   ```java
   package de.schulung.jakartaee.todos;

   import static io.restassured.RestAssured.given;

   import org.junit.jupiter.api.BeforeAll;
   import org.junit.jupiter.api.Test;

   import io.restassured.RestAssured;
   import io.restassured.http.ContentType;

   class TodosServletIT {

       @BeforeAll
       static void configureRestAssured() {
           RestAssured.baseURI = "http://localhost";
           RestAssured.port = Integer.getInteger("liberty.http.port", 9080);
           RestAssured.basePath = "/todos-app";
       }

       @Test
       void tooShortTitleIsRejected() {
           given()
                   .redirects().follow(false)
                   .contentType(ContentType.URLENC)
                   .formParam("title", "Ab")             // < 3 Zeichen -> ungültig
                   .formParam("description", "Testbeschreibung")
                   .when()
                   .post("/add-todo")
                   .then()
                   .statusCode(400);
       }

   }
   ```
   > `redirects().follow(false)` verhindert, dass REST-Assured einem Erfolgs-Redirect folgt – so prüfst Du wirklich die Antwort des `add-todo`-Servlets und nicht die der Folgeseite.
9. **Ausführen** mit `mvn verify`. Beobachte in der Ausgabe, wie Liberty startet, der Test läuft und Liberty wieder stoppt. Prüfe zur Kontrolle, dass dabei **kein** `.localdb`-Ordner entsteht – ein Beleg, dass die Tests wirklich In-Memory liefen.
   > ⚠️ **`mvn verify` und `mvn liberty:dev` nicht gleichzeitig laufen lassen.** Beide nutzen denselben Port (9080) und dasselbe Server-Verzeichnis (`defaultServer`). Läuft `liberty:dev` bereits, scheitert `verify` am belegten Port; schlimmer noch, `verify` würde den In-Memory-Override in den laufenden Dev-Server kopieren (Liberty lädt configDropins **live** nach) und ihn am Ende stoppen. Stoppe `liberty:dev` also, bevor Du `mvn verify` startest.

### Teil 4 (optional): Weitere Tests

10. Schreibe weitere Integrationstests, z. B.:
   - **Positivfall:** Ein gültiges Todo per `POST /add-todo` anlegen (Statuscode darf kein `4xx`/`5xx` sein) und anschließend per `GET /todos` prüfen, dass der Titel im zurückgelieferten HTML enthalten ist (Tipp: `body(containsString(titel))`).
   - **Weitere Fehlerfälle:** leerer Titel, Titel ohne Großbuchstaben am Anfang, ungültiges Datumsformat, Fälligkeitsdatum in der Vergangenheit oder zu weit in der Zukunft – alle sollen `400` liefern.
   - Fasse die Fehlerfälle bei Bedarf zu einem `@ParameterizedTest` zusammen, um Duplikate zu vermeiden.
11. Beantwortet gemeinsam die Reflexionsfragen.

## 📚 Selbstlernmaterial

* [Open Liberty: Testing a MicroProfile or Jakarta EE application (Guide)](https://openliberty.io/guides/rest-intro.html) — Integrationstests gegen einen laufenden Liberty
* [REST-Assured: Usage Guide](https://github.com/rest-assured/rest-assured/wiki/Usage) — Syntax und Möglichkeiten von REST-Assured
* [Maven Failsafe Plugin: Usage](https://maven.apache.org/surefire/maven-failsafe-plugin/usage.html) — Integrationstests im Maven-Lifecycle
* [H2 Database: Features & Modes](https://www.h2database.com/html/features.html) — In-Memory- vs. File-Modus und Verbindungs-URLs
* [Baeldung: Integration Testing with Maven](https://www.baeldung.com/maven-integration-test) — `integration-test`- und `verify`-Phasen erklärt

## 🤔 Reflexionsfragen

* Was ist der Unterschied zwischen einem **Blackbox-Integrationstest** und einem **Unit-Test**? Welche Fehler findet der eine, die der andere nicht findet?
* **Warum setzen wir für die Tests eine In-Memory-Datenbank ein** statt der dateibasierten `.localdb`?
* Warum laufen die Tests in der Phase `integration-test` (Failsafe) und nicht in `test` (Surefire)? Was muss vorher passiert sein, damit ein Aufruf gegen `http://localhost:9080` überhaupt funktioniert?
* Unsere Tests kennen nur die HTTP-Schnittstelle. Welche Konsequenz hat das, wenn wir später die interne Implementierung (z. B. den `TodosService`) umbauen? Welche Art von Test müsste man dann anpassen – und welche nicht?
