---
layout: default
title: Konfiguration mit MicroProfile Config
---

# Konfiguration mit MicroProfile Config

Bisher ist das Verhalten der Anwendung fest im Code verdrahtet – der
`SampleDataInitializer` legt beim Start **immer** Beispiel-Todos an. In echten
Umgebungen möchte man solches Verhalten aber **von außen** steuern können, ohne
den Code neu zu übersetzen: In der Entwicklung sind Beispieldaten praktisch, in
der Produktion wären sie ein Ärgernis. Genau dafür gibt es **MicroProfile
Config**: einen einheitlichen, herstellerunabhängigen Weg, Konfigurationswerte
aus verschiedenen Quellen (Property-Datei, Umgebungsvariablen, System-Properties
…) einzulesen und per `@Inject` in die Anwendung zu bringen. In dieser Übung
machst Du das Anlegen der Beispieldaten über eine Config-Property **abschaltbar**
– mit `false` als Konvention (also standardmäßig deaktiviert).

> Die Swagger UI (`mpOpenAPI`) hast Du bereits als MicroProfile-Feature in der
> `server.xml` aktiviert. MicroProfile Config ist ein weiteres Feature aus
> derselben Plattform.

## 🎯 Lernziele

* Du verstehst, warum man Verhalten **konfigurierbar** macht, statt es im Code fest zu verdrahten.
* Du kannst einen Konfigurationswert per `@Inject @ConfigProperty` in ein CDI-Bean injizieren und einen `defaultValue` festlegen.
* Du kennst die wichtigsten **Config-Quellen** von MicroProfile Config (`microprofile-config.properties`, Umgebungsvariablen, System-Properties) und weißt, dass sie über eine **Rangfolge (Ordinal)** priorisiert werden.
* Du kannst MicroProfile Config in Open Liberty aktivieren (`mpConfig`).

## ✅ Definition of Done

* [ ] Das Feature `mpConfig-2.0` ist in der `server.xml` aktiv und die `microprofile-config-api` ist als `provided`-Dependency im `pom.xml` eingebunden.
* [ ] Der `SampleDataInitializer` besitzt eine per `@Inject @ConfigProperty(name = "todos.sampledata.enabled", defaultValue = "false")` injizierte `boolean`-Property.
* [ ] Beim Start wird ein Log-Eintrag „Sample Data Initializer checking current database entries…" geschrieben – aber **nur**, wenn die Property aktiv ist.
* [ ] **Ohne** gesetzte Property werden **keine** Beispieldaten angelegt (Konvention: deaktiviert).
* [ ] Über eine `microprofile-config.properties` lässt sich das Anlegen der Beispieldaten einschalten (`true`).
* [ ] Ihr habt die Reflexionsfragen schriftlich beantwortet.

## 🪜 Arbeitsschritte

### Teil 1: MicroProfile Config aktivieren

1. **Feature in der `server.xml` ergänzen:**
   ```xml
   <!-- MicroProfile Config: @ConfigProperty / Config-API -->
   <feature>mpConfig-2.0</feature>
   ```
2. **API-Dependency im `pom.xml` ergänzen** (nur zur Compile-Zeit nötig; die Implementierung liefert Open Liberty):
   ```xml
   <dependency>
       <groupId>org.eclipse.microprofile.config</groupId>
       <artifactId>microprofile-config-api</artifactId>
       <version>2.0</version>
       <scope>provided</scope>
   </dependency>
   ```

### Teil 2: Beispieldaten konfigurierbar machen

3. **Property injizieren.** Ergänze im `SampleDataInitializer` ein Feld, das den Konfigurationswert aufnimmt. Der `defaultValue` sorgt dafür, dass die Anwendung auch dann startet, wenn die Property nirgends gesetzt ist – und dass die Konvention **deaktiviert** ist:
   ```java
   import org.eclipse.microprofile.config.inject.ConfigProperty;

   @Inject
   @ConfigProperty(name = "todos.sampledata.enabled", defaultValue = "false")
   private boolean sampleDataEnabled;
   ```
   > Beachte: `@ConfigProperty` stammt aus MicroProfile Config (`org.eclipse.microprofile.config.inject`), **nicht** aus CDI. Die Umwandlung des Strings `"false"` in einen `boolean` übernimmt MicroProfile Config automatisch.
4. **Logging einbauen und das Anlegen steuern.** Ergänze in der Startup-Methode ein kleines Logging und werte die Property aus. Ist sie nicht aktiv, bricht die Methode früh ab (kein DB-Zugriff, keine Beispieldaten):
   ```java
   private static final Logger LOGGER =
           Logger.getLogger(SampleDataInitializer.class.getName());

   void onStartup(@Observes @Initialized(ApplicationScoped.class) Object init) {
       if (!sampleDataEnabled) {
           LOGGER.info("Sample data initialization is disabled "
                   + "(set todos.sampledata.enabled=true to enable it).");
           return;
       }
       LOGGER.info("Sample Data Initializer checking current database entries...");
       // ... bestehende Prüfung (count() > 0) und das Anlegen der Beispiel-Todos
   }
   ```

### Teil 3: Standardverhalten prüfen (deaktiviert)

5. **Starten und beobachten.** Baue und starte die Anwendung mit `mvn liberty:dev`. Da die Property nirgends gesetzt ist, greift der `defaultValue="false"`:
   - Im Log erscheint der Hinweis, dass die Initialisierung **deaktiviert** ist.
   - Öffne [http://localhost:9080/todos-app/todos](http://localhost:9080/todos-app/todos) – es werden **keine** Beispiel-Todos angezeigt (leere, frische Datenbank vorausgesetzt).

### Teil 4: Über eine Property-Datei einschalten

6. **`microprofile-config.properties` anlegen.** Erzeuge die Datei unter `src/main/resources/META-INF/microprofile-config.properties` mit folgendem Inhalt:
   ```properties
   todos.sampledata.enabled=true
   ```
   Dies ist die von MicroProfile Config standardmäßig eingelesene, **portable** Config-Quelle (sie ist herstellerunabhängig und wird mit der Anwendung ausgeliefert).
7. **Erneut starten und prüfen.** Nach einem Neustart erscheint im Log nun „Sample Data Initializer checking current database entries…", und die Beispiel-Todos tauchen unter [http://localhost:9080/todos-app/todos](http://localhost:9080/todos-app/todos) auf.

### Teil 5 (optional): Wert per Umgebungsvariable überschreiben

MicroProfile Config liest aus **mehreren** Quellen; jede hat eine Priorität
(ein *Ordinal*). Umgebungsvariablen (Ordinal 300) haben Vorrang vor der
`microprofile-config.properties` (Ordinal 100). Damit kannst Du den in der
Property-Datei gesetzten Wert **zur Laufzeit** wieder überschreiben, ohne die
Anwendung neu zu bauen.

8. **Umgebungsvariable setzen.** Open Liberty liest Umgebungsvariablen aus einer
   `server.env` im Server-Konfigurationsverzeichnis. Über das
   `liberty-maven-plugin` legst Du sie dazu unter
   `src/main/liberty/config/server.env` an (sie wird in den Server kopiert):
   ```properties
   TODOS_SAMPLEDATA_ENABLED=false
   ```
   > MicroProfile Config bildet den Property-Namen `todos.sampledata.enabled` auf
   > den Variablennamen `TODOS_SAMPLEDATA_ENABLED` ab (Punkte → Unterstriche,
   > Großschreibung).
9. **Neu starten und beobachten.** Obwohl die `microprofile-config.properties`
   weiterhin `true` enthält, „gewinnt" die Umgebungsvariable: Im Log steht wieder
   der Hinweis, dass die Initialisierung deaktiviert ist. Damit hast Du die
   **Config-Rangfolge** in Aktion gesehen.
10. Beantwortet gemeinsam die Reflexionsfragen.

## 📚 Selbstlernmaterial

* [MicroProfile Config: Specification](https://download.eclipse.org/microprofile/microprofile-config-2.0/microprofile-config-spec-2.0.html) — die MicroProfile-Config-Spezifikation, u.a. mit den Regeln zu Config-Quellen und Ordinal
* [Open Liberty: MicroProfile Config feature](https://openliberty.io/docs/latest/reference/feature/mpConfig-2.0.html) — Aktivierung von `mpConfig` in Liberty
* [Open Liberty: External configuration of microservices](https://openliberty.io/docs/latest/external-configuration.html) — welche Config-Quellen Liberty kennt und wie sie priorisiert werden
* [Open Liberty Guide: Configuring microservices](https://openliberty.io/guides/microprofile-config.html) — `@ConfigProperty` end-to-end
* [Baeldung: MicroProfile Config](https://www.baeldung.com/microprofile) — `@ConfigProperty`, Config-Quellen und Konvertierung in der Praxis

## 🤔 Reflexionsfragen

* Warum ist es sinnvoll, Verhalten wie das Anlegen von Beispieldaten über Konfiguration zu steuern?
* Wiederhole noch einmal den Begriff _Convention over Configuration_ und erkläre am Beispiel dieser Übung nochmal, wo Standardwerte gesetzt und überschrieben werden können.
