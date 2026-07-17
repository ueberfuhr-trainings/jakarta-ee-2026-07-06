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
* Du kannst die drei Frameworks anhand einheitlicher Kriterien (HTTP-Verarbeitung, REST, JSON, Validierung, DI, Persistenz, Tests, Konfiguration) in einer Tabelle gegenüberstellen.
* Du kannst eine REST-API mit `curl` (oder der Swagger UI) ausprobieren.

## ✅ Definition of Done

* [ ] Du hast alle drei Projekte gestartet und die Startseite bzw. die REST-API im Browser/mit `curl` erreicht.
* [ ] Du hast in jedem Projekt ein Todo per REST angelegt, ausgelesen und gelöscht.
* [ ] Du hast die Vergleichstabelle für **Spring** und **Quarkus** ausgefüllt (die Jakarta-EE-Spalte ist vorgegeben).
* [ ] Ihr habt die Reflexionsfragen schriftlich beantwortet.

## 🪜 Arbeitsschritte

### Teil 1: Die drei Projekte starten

Alle drei Projekte laufen bewusst unter **derselben Adresse** – Port **9080** und
Context-Root **`/todos-app`**. Sie lassen sich deshalb **nicht gleichzeitig**
betreiben: Starte immer nur **ein** Projekt, sieh es Dir an und **stoppe** es mit
`Strg+C`, bevor Du das nächste startest.

| Kriterium       | Jakarta EE (`todos-app`)                                               | Spring (`todos-app-spring`) | Quarkus (`todos-app-quarkus`) |
|-----------------|------------------------------------------------------------------------|-----------------------------|-------------------------------|
| JDK-Version     | 8                                                                      | 8                           | 17                            |
| Startbefehl     | `mvn liberty:dev`                                                      | `mvn liberty:dev`           | `mvn quarkus:dev`             |
| Startseite (UI) | [`http://localhost:9080/todos-app/`](http://localhost:9080/todos-app/) | *dieselbe URL*              | – (keine UI, nur REST)        |
| REST-API        | `http://localhost:9080/todos-app/api/todos`                            | *dieselbe URL*              | *dieselbe URL*                |
| Tests ausführen | `mvn verify`                                                           | `mvn test`                  | `mvn test`                    |

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
   - Quarkus zusätzlich – Dev UI: [http://localhost:9080/todos-app/q/dev](http://localhost:9080/todos-app/q/dev) 
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

### Teil 3: Vergleichstabelle ausfüllen

**Fülle die folgende Tabelle für Spring und Quarkus aus.** Die Spalte
*Jakarta EE / Liberty* ist als Referenz bereits vorgegeben; die Spalten *Spring*
und *Quarkus* erarbeitest Du selbst, indem Du Dir den Code und die
Konfigurationsdateien der jeweiligen Projekte ansiehst.

| Kriterium                      | Jakarta EE / Liberty                                                                                   | Jakarta EE / Liberty mit Spring-Integration | Quarkus |
|--------------------------------|--------------------------------------------------------------------------------------------------------|---------------------------------------------|---------|
| Verarbeitung von HTTP-Anfragen | Servlets                                                                                               |                                             |         |
| Implementierung REST-API       | JAX-RS Resource                                                                                        |                                             |         |
| JSON-Serialisierung            | JSON-B                                                                                                 |                                             |         |
| Bean Validation verfügbar?     | ✅                                                                                                      |                                             |         |
| Dependency Injection           | CDI                                                                                                    |                                             |         |
| Datenbankzugriffe              | - JPA (EclipseLink)<br/>- EntityManager                                                                |                                             |         |
| Integrationstests              | - Phase `integration-test`<br/>- Liberty mit In-Memory-Datasource<br/>- REST-Assured                   |                                             |         |
| Konfigurationsdateien          | - `server.xml`/`.env`<br/>- `persistence.xml`<br/>- `beans.xml`<br/>- `microprofile-config.properties` |                                             |         |

Beantwortet anschließend gemeinsam die Reflexionsfragen.

## 📚 Selbstlernmaterial

* [Spring Framework: Web MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html) — REST-Controller mit Spring MVC
* [Spring Data JPA: Reference](https://docs.spring.io/spring-data/jpa/reference/jpa.html) — Repository-Abstraktion (`JpaRepository`, abgeleitete Query-Methoden)
* [Quarkus: Getting Started](https://quarkus.io/guides/getting-started) — erste Schritte, `quarkus:dev`, Dev UI
* [Quarkus: Simplified Hibernate ORM with Panache](https://quarkus.io/guides/hibernate-orm-panache) — Panache, inkl. Repository-Ansatz (`PanacheRepository`)
* [Quarkus: Writing JSON REST Services](https://quarkus.io/guides/rest-json) — JAX-RS/REST in Quarkus
* [Jakarta EE vs. Spring vs. MicroProfile/Quarkus](https://www.baeldung.com/spring-vs-java-ee) — Einordnung der Ökosysteme

## 🤔 Reflexionsfragen

* Bei welchen Kriterien der Tabelle ähneln sich die Projekte am stärksten, bei welchen unterscheiden sie sich am deutlichsten?
* Was bedeutet _Single File Configuration_? In welchem der 3 Projekte liegt diese vor?
* Bei welchem der Projekte muss für die Tests der Liberty gestartet werden?
