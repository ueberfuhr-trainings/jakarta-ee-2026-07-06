---
layout: default
title: Frameworks im Vergleich – Jakarta EE, Spring, Quarkus
---

# Frameworks im Vergleich – Jakarta EE, Spring, Quarkus

Die Todo-Anwendung hast Du bisher mit **Jakarta EE** gebaut. Dieselbe Fachlichkeit
lässt sich aber auch mit anderen Technologien umsetzen – und genau das schaust Du
Dir in dieser Übung an. Es geht **nicht** ums Programmieren, sondern ums
**Vergleichen**: Neben dem Original `todos-app` (Jakarta EE) liegen zwei
Nachbauten bereit – `todos-app-spring` (**Spring Framework 5**, ohne Spring Boot,
als WAR auf demselben Open Liberty) und `todos-app-quarkus` (**Quarkus**). Du
startest die Projekte, probierst ihre REST-APIs aus und arbeitest anhand
vorgegebener Fragen die Gemeinsamkeiten und Unterschiede heraus.

> Alle drei Projekte lösen dieselbe Aufgabe (Todos anlegen, auslesen, löschen).
> Die **Fachlogik** (Domänenmodell, Status-Werte, Validierungsregeln) ist bewusst
> gleich gehalten; unterschiedlich ist, **wie** die Frameworks die technischen
> Aufgaben lösen.

## 🎯 Lernziele

* Du verstehst, dass sich dieselbe Fachlichkeit mit verschiedenen Frameworks (Jakarta EE, Spring, Quarkus) umsetzen lässt.
* Du kannst benennen, wie die drei Frameworks jeweils REST-Endpunkte, Validierung, Datenbankzugriff und Konfiguration umsetzen.
* Du kennst zentrale Unterschiede – z.B. Spring MVC vs. JAX-RS, Spring Data JPA vs. Panache-Repository, `server.xml`/`persistence.xml` vs. `application.properties`.
* Du kannst eine REST-API mit `curl` (oder der Swagger UI) ausprobieren.

## ✅ Definition of Done

* [ ] Du hast alle drei Projekte gestartet und die Startseite bzw. die REST-API im Browser/mit `curl` erreicht.
* [ ] Du hast in jedem Projekt ein Todo per REST angelegt, ausgelesen und gelöscht.
* [ ] Du hast für jede der vier Leitfragen (REST, Validierung, Datenbank, Konfiguration) die entsprechenden Stellen in allen drei Projekten gefunden und verglichen.
* [ ] Ihr habt die Reflexionsfragen schriftlich beantwortet.

## 🪜 Arbeitsschritte

### Teil 1: Die drei Projekte starten

Alle drei Projekte laufen bewusst unter **derselben Adresse** – Port **9080** und
Context-Root **`/todos-app`**. Sie lassen sich deshalb **nicht gleichzeitig**
betreiben: Starte immer nur **ein** Projekt, sieh es Dir an und **stoppe** es mit
`Strg+C`, bevor Du das nächste startest.

| Kriterium | Jakarta EE (`todos-app`) | Spring (`todos-app-spring`) | Quarkus (`todos-app-quarkus`) |
|---|---|---|---|
| Startseite (UI) | [`http://localhost:9080/todos-app/`](http://localhost:9080/todos-app/) | *dieselbe URL* | – (keine UI, nur REST) |
| REST-API | `http://localhost:9080/todos-app/api/todos` | *dieselbe URL* | *dieselbe URL* |
| Startbefehl | `mvn liberty:dev` | `mvn liberty:dev` | `mvn quarkus:dev` |
| Tests ausführen | `mvn verify` (REST-Assured-IT gegen den laufenden Liberty) | `mvn test` (Spring MockMvc, in-JVM) | `mvn test` (`@QuarkusTest`, in-JVM) |

1. **Ein Projekt starten** – je nach Variante eines von:
   ```bash
   cd todos-app          && mvn liberty:dev     # Jakarta EE
   cd todos-app-spring   && mvn liberty:dev     # Spring
   cd todos-app-quarkus  && mvn quarkus:dev     # Quarkus
   ```
2. **Im Browser öffnen:**
   - Startseite/UI (nur Jakarta EE & Spring): [http://localhost:9080/todos-app/](http://localhost:9080/todos-app/)
   - REST-API: `http://localhost:9080/todos-app/api/todos`
   - Jakarta EE zusätzlich – Swagger UI: [http://localhost:9080/openapi/ui](http://localhost:9080/openapi/ui)
   - Quarkus zusätzlich – Dev UI (die genaue URL zeigt Quarkus beim Start in der Konsole, unter `/q/dev/`)
   - **Hinweis Quarkus:** enthält **bewusst keine** server-gerenderte HTML-/JSP-Oberfläche – Quarkus setzt nicht mehr auf JSPs (moderne Templating-Engine dort wäre z.B. *Qute*). Du sprichst diese App nur über die REST-API an.
3. **Zum Wechseln** das laufende Projekt mit `Strg+C` stoppen, dann das nächste starten.

### Teil 2: Die REST-APIs ausprobieren

4. **Todos anlegen, auslesen und löschen** – exakt **dieselben** Aufrufe funktionieren gegen jede der drei Varianten (gleiche URL, nur ein Projekt läuft):
   ```bash
   # anlegen -> 201 mit Location-Header
   curl -i -X POST http://localhost:9080/todos-app/api/todos \
     -H "Content-Type: application/json" \
     -d '{"title":"Vergleich testen","description":"per curl","dueDate":"2026-08-01"}'

   # alle Todos auslesen
   curl -i http://localhost:9080/todos-app/api/todos

   # ein einzelnes Todo (id anpassen) bzw. nicht vorhandenes -> 404
   curl -i http://localhost:9080/todos-app/api/todos/1
   curl -i http://localhost:9080/todos-app/api/todos/9999

   # löschen -> 204
   curl -i -X DELETE http://localhost:9080/todos-app/api/todos/1

   # ungültige Eingabe (zu kurzer Titel) -> 400
   curl -i -X POST http://localhost:9080/todos-app/api/todos \
     -H "Content-Type: application/json" -d '{"title":"Ab"}'
   ```
   - Achte darauf, dass Statuscodes (`201`, `200`, `204`, `400`, `404`) und JSON-Ausgabe (inkl. der Status-Werte `ready`/`in_progress`/`done`) in allen drei Projekten **gleich** sind – obwohl die Frameworks intern völlig unterschiedlich arbeiten.

### Teil 3: Anhand der Leitfragen vergleichen

5. **Arbeite die folgenden vier Leitfragen für alle drei Projekte durch** und halte Deine Beobachtungen fest. In Klammern stehen Startpunkte für die Code-Suche.

   1. **Wie wird die REST-API implementiert?**
      - Jakarta EE: JAX-RS – `boundary/rest/TodosResource.java`, `RestApplication.java` (`@Path`, `@GET/@POST/@DELETE`, `Response`).
      - Spring: Spring MVC – `boundary/rest/TodosRestController.java` (`@RestController`, `@GetMapping/@PostMapping/@DeleteMapping`, `ResponseEntity`).
      - Quarkus: JAX-RS (Quarkus REST) – `boundary/rest/TodosResource.java` (`@Path`, wie Jakarta EE, aber im `jakarta.*`-Namespace).
   2. **Wie wird validiert?**
      - In allen dreien Bean Validation (`@NotNull`, das Composite-Constraint `@Title`, das eigene `@MaximumFuture`) am `TodoDto` bzw. am Domänenmodell.
      - Vergleiche: Wo steht `@Valid`? Wie kommt der Fehler zum `400`? Was ist gleich, was ist der Namespace-Unterschied (`javax.validation` vs. `jakarta.validation`)?
   3. **Wie sehen Datenbankzugriffe aus?**
      - Jakarta EE: Domänen-Interface `domain/TodosDao.java`, implementiert in `persistence/JpaTodosDao.java` mit dem `EntityManager`.
      - Spring: **dieselbe Schichtung** – Domänen-Interface `domain/TodosDao.java`, implementiert in `persistence/JpaTodosDao.java`; dort wird aber ein **Spring-Data-JPA-Repository** (`persistence/TodoRepository.java`, Interface `extends JpaRepository`, Methoden werden generiert) statt eines `EntityManager` genutzt. Die dahinterliegende `EntityManagerFactory` stellt allerdings der Liberty bereit (siehe Konfiguration).
      - Quarkus: **Panache mit Repository-Ansatz** – `persistence/TodoRepository.java` (`implements PanacheRepository<TodoEntity>`), genutzt vom Service.
      - Vergleiche: In Jakarta EE und Spring definiert die **Domäne** das DAO-Interface (Dependency Inversion), die Persistenz liefert die Implementierung; bei Quarkus greift der Service direkt auf das Panache-Repository zu.
   4. **Wo stehen die Konfigurationen?**
      - Jakarta EE: `src/main/liberty/config/server.xml` (Features, DataSource), `src/main/resources/META-INF/persistence.xml` (Persistence-Unit), `META-INF/microprofile-config.properties`.
      - Spring: gemischt – Java-Konfiguration (`AppConfig`/`WebAppInitializer` im Wurzelpaket, `boundary/WebConfig.java`, `persistence/PersistenceConfig.java`) + `application.properties`. **JPA und DataSource kommen aber – wie bei Jakarta EE – vom Liberty**: `server.xml` (DataSource `jdbc/todos`) und `persistence.xml`; ein `persistence-unit-ref` in `web.xml` bindet die `EntityManagerFactory` ins JNDI, wo Spring sie in `persistence/LibertyPersistenceConfig.java` abholt.
      - Quarkus: alles gebündelt in `src/main/resources/application.properties` (Port/Root-Path, DataSource, Hibernate, eigene Properties).

6. Beantwortet gemeinsam die Reflexionsfragen.

## 📚 Selbstlernmaterial

* [Spring Framework: Web MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html) — REST-Controller mit Spring MVC
* [Spring Data JPA: Reference](https://docs.spring.io/spring-data/jpa/reference/jpa.html) — Repository-Abstraktion (`JpaRepository`, abgeleitete Query-Methoden)
* [Quarkus: Getting Started](https://quarkus.io/guides/getting-started) — erste Schritte, `quarkus:dev`, Dev UI
* [Quarkus: Simplified Hibernate ORM with Panache](https://quarkus.io/guides/hibernate-orm-panache) — Panache, inkl. Repository-Ansatz (`PanacheRepository`)
* [Quarkus: Writing JSON REST Services](https://quarkus.io/guides/rest-json) — JAX-RS/REST in Quarkus
* [Jakarta EE vs. Spring vs. MicroProfile/Quarkus](https://www.baeldung.com/spring-vs-java-ee) — Einordnung der Ökosysteme

## 🤔 Reflexionsfragen

* Bei welchen der vier Leitfragen ähneln sich die Projekte am stärksten, bei welchen unterscheiden sie sich am deutlichsten? Woran liegt das?
* Jakarta EE und Quarkus nutzen beide JAX-RS und Bean Validation. Was ist der auffälligste Unterschied im Code – und was hat es mit `javax.*` vs. `jakarta.*` auf sich?
* Vergleiche Datenzugriff über den `EntityManager` (Jakarta EE) mit Spring Data JPA und Panache-Repository: Wie viel Code musst Du jeweils selbst schreiben, und was übernimmt das Framework?
* In der Jakarta-EE-App ist die Konfiguration über mehrere Dateien verteilt (`server.xml`, `persistence.xml`, `microprofile-config.properties`), bei Quarkus steht fast alles in einer `application.properties`. Welche Vor- und Nachteile hat das jeweils?
* Warum lässt sich die Spring-App auf demselben Open Liberty betreiben wie die Jakarta-EE-App, obwohl sie „kein Jakarta EE" ist? Was bringt Spring selbst mit (DI, MVC, Transaktionsklammer), und was übernimmt weiterhin der Server – insbesondere: Spring holt sich die JPA-`EntityManagerFactory` und die DataSource per JNDI vom Liberty. Welche Vor-/Nachteile hat das gegenüber einer von Spring selbst verwalteten DataSource?
* Die Quarkus-Variante verzichtet auf eine server-gerenderte JSP-Oberfläche. Warum passt das zu einem modernen, API-orientierten Ansatz – und wie würde man eine UI heute typischerweise anbinden?
