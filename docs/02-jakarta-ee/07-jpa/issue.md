---
layout: default
title: Datenbankzugriff mit JPA
---

# Datenbankzugriff mit JPA

Bisher hält der `TodosService` die Todos in einer In-Memory-Collection – bei
jedem Neustart sind sie weg. In dieser Übung persistierst Du die Todos in eine
**Datenbank**: Du konfigurierst eine dateibasierte **H2**-Datenbank samt
**Datasource** in Open Liberty, aktivierst **JPA**, machst das `Todo` per
Annotationen zu einer **Entity** und lässt Dir im Service einen
**`EntityManager`** injizieren. Auf eine Schichtentrennung (eigenes
Persistenz-Entity vs. Domänenobjekt) verzichten wir vorerst bewusst – wir
annotieren direkt das vorhandene `Todo`.

## 🎯 Lernziele

* Du kannst eine dateibasierte H2-Datenbank und eine Datasource in Open Liberty konfigurieren.
* Du kannst eine Klasse mit JPA-Annotationen zu einer Entity machen und mit dem `EntityManager` speichern und lesen.
* Du verstehst das Zusammenspiel von Datasource, Persistence-Unit und `EntityManager`.

## ✅ Definition of Done

* [ ] In Liberty ist das JPA-Feature aktiv, der H2-Treiber verfügbar und eine Datasource (`jdbc/todos`) konfiguriert.
* [ ] Die H2-Datenbank ist dateibasiert und liegt im Projektordner `.localdb`.
* [ ] Die Anwendung speichert die Daten in der lokalen Datenbank.
* [ ] Ihr habt die Reflexionsfragen schriftlich beantwortet.

## 🪜 Arbeitsschritte

### Teil 1: H2-Datenbank in Liberty konfigurieren

1. **H2-Treiber als Dependency** in die `pom.xml` aufnehmen (`provided`, da nicht ins WAR, sondern in den Server gehört):
   ```xml
   <dependency>
       <groupId>com.h2database</groupId>
       <artifactId>h2</artifactId>
       <version>1.4.200</version>
       <scope>provided</scope>
   </dependency>
   ```
   > Tipp: Nimm bewusst H2 **1.4.200**. Neuere H2-2.x-Versionen lehnen das von EclipseLink (dem JPA-Provider in Liberty) erzeugte `CREATE TABLE ... BIGINT IDENTITY` mit einem Syntaxfehler ab.
2. **Treiber für Liberty bereitstellen und DB-Pfad festlegen.** Erweitere das `liberty-maven-plugin` um eine `<configuration>`, die die H2-JAR in die geteilten Ressourcen des Servers kopiert und den Datenbank-Ordner als Liberty-Variable auf den Projektordner `.localdb` setzt:
   ```xml
   <configuration>
       <copyDependencies>
           <dependencyGroup>
               <location>${project.build.directory}/liberty/wlp/usr/shared/resources</location>
               <dependency>
                   <groupId>com.h2database</groupId>
                   <artifactId>h2</artifactId>
               </dependency>
           </dependencyGroup>
       </copyDependencies>
       <bootstrapProperties>
           <todos.db.dir>${project.basedir}/.localdb</todos.db.dir>
       </bootstrapProperties>
   </configuration>
   ```
   > Tipp: Trage `.localdb/` in die `.gitignore` ein – die lokale Datenbank gehört nicht ins Repository.
3. **`server.xml` erweitern.** Aktiviere das JPA-Feature und konfiguriere Treiber-Library und Datasource:
   ```xml
   <featureManager>
       <!-- ... bestehende Features ... -->
       <feature>jpa-2.2</feature>
   </featureManager>

   <library id="h2">
       <fileset dir="${shared.resource.dir}" includes="h2-*.jar"/>
   </library>

   <dataSource id="todosDS" jndiName="jdbc/todos">
       <jdbcDriver libraryRef="h2"/>
       <properties URL="jdbc:h2:file:${todos.db.dir}/todos;AUTO_SERVER=TRUE" user="sa" password="sa"/>
   </dataSource>
   ```
   > `file:` macht die Datenbank dateibasiert (persistent auf der Platte), `AUTO_SERVER=TRUE` erlaubt zusätzlich parallele Verbindungen (z. B. ein DB-Tool).

### Teil 2: JPA im `TodosService` nutzen

4. **`persistence.xml` anlegen** unter `src/main/resources/META-INF/persistence.xml`. Die Persistence-Unit verweist auf die Datasource und listet das `Todo` als Entity:
   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence
                                    http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd"
                version="2.2">
       <persistence-unit name="todos" transaction-type="JTA">
           <jta-data-source>jdbc/todos</jta-data-source>
           <class>de.schulung.jakartaee.todos.Todo</class>
           <properties>
               <property name="javax.persistence.schema-generation.database.action" value="create"/>
           </properties>
       </persistence-unit>
   </persistence>
   ```
   > Wir nutzen bewusst `create` (nur anlegen), nicht `drop-and-create` – sonst wären die Daten nach jedem Neustart wieder weg.
5. **`Todo` zur Entity machen.** Annotiere die Klasse mit `@Entity` und ergänze einen **Primärschlüssel** (Tipp: ein Feld `id` mit `@Id` und `@GeneratedValue`). Denke an die Besonderheiten:
    - Der Status ist ein Enum – überlege, wie er gespeichert werden soll (Tipp: `@Enumerated(EnumType.STRING)`).
    - Die vorhandenen Bean-Validation-Annotationen dürfen bleiben; JPA und Bean Validation koexistieren am selben Feld.
    - JPA benötigt einen parameterlosen Konstruktor (ist bereits vorhanden).
6. **`TodosService` auf JPA umstellen.** Ersetze die In-Memory-Collection:
    - Lass Dir einen `EntityManager` injizieren (Tipp: `@PersistenceContext`).
    - Speichere im Hinzufügen (Tipp: `em.persist(todo)`). Schreibende Operationen laufen in einer Transaktion – markiere die Methode passend (Tipp: `@Transactional`).
    - Lies alle Todos per JPQL (Tipp: `SELECT t FROM Todo t`). Auch die Titelsuche lässt sich als JPQL mit `LIKE` formulieren.
7. **Testen.**
    - Lege über das Formular ein Todo an und prüfe die Anzeige.
    - **Stoppe den Server und starte ihn neu.** Das Todo muss weiterhin vorhanden sein – der Beweis, dass es persistiert wurde.
    - Wirf einen Blick in den Ordner `.localdb` – dort liegt jetzt die H2-Datenbankdatei.

## 📚 Selbstlernmaterial

* [Jakarta Persistence: Specification](https://jakarta.ee/specifications/persistence/) — die JPA-Spezifikation
* [Open Liberty: Accessing a database with JPA (Guide)](https://openliberty.io/guides/jpa-intro.html) — JPA in Liberty end-to-end
* [Open Liberty: Relational database connections (dataSource)](https://openliberty.io/docs/latest/relational-database-connections-JDBC.html) — Datasource-Konfiguration
* [Baeldung: JPA EntityManager](https://www.baeldung.com/hibernate-entitymanager) — Umgang mit dem EntityManager
* [H2 Database: Features & Modes](https://www.h2database.com/html/features.html) — Embedded/File-Modus und Verbindungs-URLs

## 🤔 Reflexionsfragen

* Wie hängen **Datasource**, **Persistence-Unit** und **`EntityManager`** zusammen? Wer stellt was bereit?
* Warum überleben die Todos jetzt einen Neustart, während die In-Memory-Collection das nicht tat?
* Was bedeutet `transaction-type="JTA"`, und warum brauchen schreibende Operationen (`persist`) eine Transaktion, lesende in der Regel nicht?
* Was bewirkt die Einstellung `schema-generation` mit dem Wert `create`? Warum wäre `drop-and-create` für die Entwicklung bequem, hier aber unpassend – und in Produktion sogar gefährlich?
* Wir annotieren die Domänenklasse `Todo` gleichzeitig mit JPA- und Bean-Validation-Annotationen (keine Schichtentrennung). Welche Vor- und Nachteile hat das?
* Der `EntityManager` wird per `@PersistenceContext` in einen `@ApplicationScoped`-Service injiziert. Warum ist das unbedenklich, obwohl ein `EntityManager` selbst nicht thread-sicher ist?
