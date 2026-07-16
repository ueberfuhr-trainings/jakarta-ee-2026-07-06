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

1. **Jakarta EE – `todos-app`** starten:
   ```bash
   cd todos-app
   mvn liberty:dev
   ```
   - UI: [http://localhost:9080/todos-app/todos](http://localhost:9080/todos-app/todos)
   - REST: `http://localhost:9080/todos-app/api/todos`
   - Swagger UI: [http://localhost:9080/openapi/ui](http://localhost:9080/openapi/ui)
2. **Spring – `todos-app-spring`** starten (Spring als WAR auf demselben Liberty):
   ```bash
   cd todos-app-spring
   mvn liberty:dev
   ```
   - UI: [http://localhost:9090/todos-app-spring/todos](http://localhost:9090/todos-app-spring/todos)
   - REST: `http://localhost:9090/todos-app-spring/api/todos`
3. **Quarkus – `todos-app-quarkus`** starten:
   ```bash
   cd todos-app-quarkus
   mvn quarkus:dev
   ```
   - REST: `http://localhost:8080/api/todos`
   - Dev UI: [http://localhost:8080/q/dev/](http://localhost:8080/q/dev/)
   - **Hinweis:** Die Quarkus-Variante enthält **bewusst keine** server-gerenderte HTML-/JSP-Oberfläche – Quarkus setzt nicht mehr auf JSPs (moderne Templating-Engine dort wäre z.B. *Qute*). Du sprichst diese App also nur über die REST-API an.

> Die Ports unterscheiden sich bewusst (9080 / 9090 / 8080), damit Du auch mehrere
> Projekte gleichzeitig laufen lassen kannst.

### Teil 2: Die REST-APIs ausprobieren

4. **Todos anlegen, auslesen und löschen** – dieselben Aufrufe gegen jede API. Beispiel Jakarta EE (Port/Context-Root für Spring bzw. Quarkus entsprechend anpassen):
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
   - Für **Spring**: `http://localhost:9090/todos-app-spring/api/todos`
   - Für **Quarkus**: `http://localhost:8080/api/todos`
   - Achte darauf, dass Statuscodes (`201`, `200`, `204`, `400`, `404`) und JSON-Ausgabe (inkl. der Status-Werte `ready`/`in_progress`/`done`) in allen drei Projekten **gleich** sind.

### Teil 3: Anhand der Leitfragen vergleichen

5. **Arbeite die folgenden vier Leitfragen für alle drei Projekte durch** und halte Deine Beobachtungen fest. In Klammern stehen Startpunkte für die Code-Suche.

   1. **Wie wird die REST-API implementiert?**
      - Jakarta EE: JAX-RS – `boundary/rest/TodosResource.java`, `RestApplication.java` (`@Path`, `@GET/@POST/@DELETE`, `Response`).
      - Spring: Spring MVC – `boundary/rest/TodosController.java` (`@RestController`, `@GetMapping/@PostMapping/@DeleteMapping`, `ResponseEntity`).
      - Quarkus: JAX-RS (Quarkus REST) – `boundary/rest/TodosResource.java` (`@Path`, wie Jakarta EE, aber im `jakarta.*`-Namespace).
   2. **Wie wird validiert?**
      - In allen dreien Bean Validation (`@NotNull`, das Composite-Constraint `@Title`, das eigene `@MaximumFuture`) am `TodoDto` bzw. am Domänenmodell.
      - Vergleiche: Wo steht `@Valid`? Wie kommt der Fehler zum `400`? Was ist gleich, was ist der Namespace-Unterschied (`javax.validation` vs. `jakarta.validation`)?
   3. **Wie sehen Datenbankzugriffe aus?**
      - Jakarta EE: JPA direkt über den `EntityManager` – `persistence/JpaTodosDao.java`.
      - Spring: **Spring Data JPA** – `persistence/TodoRepository.java` (Interface `extends JpaRepository`, Methoden werden generiert).
      - Quarkus: **Panache mit Repository-Ansatz** – `persistence/TodoRepository.java` (`implements PanacheRepository<TodoEntity>`).
   4. **Wo stehen die Konfigurationen?**
      - Jakarta EE: `src/main/liberty/config/server.xml` (Features, DataSource), `src/main/resources/META-INF/persistence.xml` (Persistence-Unit), `META-INF/microprofile-config.properties`.
      - Spring: Java-Konfiguration (`config/PersistenceConfig.java`, `config/WebConfig.java`, `config/WebAppInitializer.java`) + `application.properties`; die `server.xml` ist minimal (nur Servlet/JSP).
      - Quarkus: alles gebündelt in `src/main/resources/application.properties` (DataSource, Hibernate, eigene Properties).

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
* Warum lässt sich die Spring-App auf demselben Open Liberty betreiben wie die Jakarta-EE-App, obwohl sie „kein Jakarta EE" ist? Was bringt Spring selbst mit, und was übernimmt weiterhin der Server?
* Die Quarkus-Variante verzichtet auf eine server-gerenderte JSP-Oberfläche. Warum passt das zu einem modernen, API-orientierten Ansatz – und wie würde man eine UI heute typischerweise anbinden?
