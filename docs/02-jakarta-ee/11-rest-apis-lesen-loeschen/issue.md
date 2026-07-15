---
layout: default
title: REST API – Todos auslesen und löschen
---

# REST API – Todos auslesen und löschen

In der vorigen Übung hast Du den ersten REST-Endpunkt gebaut: das Anlegen eines
Todos per `POST`. Jetzt vervollständigst Du die API um das **Auslesen** (alle und
einzeln) und das **Löschen**. Du arbeitest dabei **test-getrieben**: Die
passenden Tests werden Dir bereitgestellt, und Du implementierst so lange, bis
sie grün sind.

> Voraussetzung: die vorige Übung „REST API – Todos anlegen (POST)" ist
> abgeschlossen (JAX-RS aktiv, `Application` mit `/api`, `TodoDto` + Mapper,
> `TodosResource`).

> 🧪 **Test-driven:** Die REST-Assured-Tests werden Dir separat über einen Pull
> Request bereitgestellt. Hol sie ins Projekt, lass sie zunächst rot laufen und
> implementiere, bis sie grün sind.

## 🎯 Lernziele

* Du kannst Lese- und Lösch-Operationen als REST-Endpunkte mit passenden HTTP-Methoden und Statuscodes umsetzen.
* Du verstehst den Unterschied zwischen Sammel- und Einzel-Ressource (`/todos` vs. `/todos/{id}`) und den Umgang mit „nicht gefunden" (`404`).
* Du kannst test-getrieben gegen vorgegebene Tests arbeiten und die API mit `curl` prüfen.

## ✅ Definition of Done

* [ ] `GET /api/todos` liefert alle Todos als JSON.
* [ ] `GET /api/todos/{id}` liefert ein einzelnes Todo als JSON – bzw. `404`, wenn es das Todo nicht gibt.
* [ ] `DELETE /api/todos/{id}` löscht ein Todo (`204 No Content`) – bzw. `404`, wenn es das Todo nicht gibt.
* [ ] Die bereitgestellten REST-Assured-Tests laufen grün (`mvn verify`).
* [ ] Ihr habt die Reflexionsfragen schriftlich beantwortet.

## 🪜 Arbeitsschritte

### Teil 1: Auslesen (`GET`)

1. **`GET`-Endpunkte** in der `TodosResource` ergänzen – alle Todos und ein einzelnes per `id`:
   ```java
   @GET
   public Collection<TodoDto> findAll() {
       // alle Todos holen und per Mapper zu TodoDto umwandeln
   }

   @GET
   @Path("{id}")
   public TodoDto findOne(@PathParam("id") long id) {
       // Todo per id holen; wenn nicht vorhanden: throw new NotFoundException();  -> 404
   }
   ```
   > Die Ausgabe erfolgt wie beim Anlegen über den Mapper (`Todo` → `TodoDto`).

### Teil 2: Löschen (`DELETE`)

2. **`DELETE`-Endpunkt** ergänzen. Ohne Rückgabewert antwortet JAX-RS mit `204 No Content`:
   ```java
   @DELETE
   @Path("{id}")
   public void delete(@PathParam("id") long id) {
       // löschen; wenn es die id nicht gibt: throw new NotFoundException();
   }
   ```
   > Überlege: Warum passt gerade `DELETE` (und nicht `POST` oder `GET`) zu dieser Operation?

### Teil 3: Testen (test-driven & mit curl)

3. **Bereitgestellte Tests nutzen.** Hol Dir die per Pull Request bereitgestellten REST-Assured-Tests ins Projekt und führe sie mit `mvn verify` aus. Implementiere so lange, bis alle grün sind.
4. **Zwischendurch mit `curl` prüfen** (Kontextpfad `/todos-app`, API-Basis `/api`), z.B. in der Git Bash:
   ```bash
   # alle Todos
   curl -i http://localhost:9080/todos-app/api/todos

   # ein einzelnes Todo (hier id = 1)
   curl -i http://localhost:9080/todos-app/api/todos/1

   # nicht vorhandenes Todo -> 404
   curl -i http://localhost:9080/todos-app/api/todos/9999

   # ein Todo löschen -> 204
   curl -i -X DELETE http://localhost:9080/todos-app/api/todos/1
   ```
   > `-i` zeigt die Antwort inklusive Statuszeile und Header.

5. Beantwortet gemeinsam die Reflexionsfragen.

## 📚 Selbstlernmaterial

* [Jakarta REST (JAX-RS): Specification](https://jakarta.ee/specifications/restful-ws/) — die JAX-RS-Spezifikation
* [Open Liberty: Creating a RESTful web service (Guide)](https://openliberty.io/guides/rest-intro.html) — JAX-RS in Liberty end-to-end
* [Baeldung: JAX-RS Basics](https://www.baeldung.com/jax-rs-spec-and-implementations) — Überblick über JAX-RS
* [MDN: HTTP request methods](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods) — Semantik von GET, POST, PUT, PATCH, DELETE

## 🤔 Reflexionsfragen

* Was ist der Unterschied zwischen den Statuscodes `200`, `201` und `204`?
* Warum liefert ein Zugriff auf ein nicht vorhandenes Todo `404` – und nicht z.B. `200` mit leerem Body?
* Welchen Statuscode liefert  `GET /api/todos`, wenn keine Todos vorhanden sind?
* Warum ist `DELETE` idempotent und `POST` nicht? Was bedeutet das für einen Client, der einen Aufruf (z.B. nach einem Timeout) wiederholt?
* Könnten wir die REST-API auch ohne JAX-RS, also rein mit Servlets implementieren?
