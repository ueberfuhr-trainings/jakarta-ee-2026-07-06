---
layout: default
title: REST APIs mit JAX-RS
---

# REST APIs mit JAX-RS

Bisher spricht die Anwendung mit dem Browser über Servlets und JSPs – die
Antwort ist fertiges HTML. Moderne Clients (z.B. Single Page Applications oder
mobile Apps) wollen aber lieber **Daten** (JSON) statt HTML und sprechen den
Server über eine **REST-API** an. In dieser Übung stellst Du die Todos zusätzlich
als REST-Ressourcen bereit: Todos **auslesen** (alle und einzeln) und **löschen**
– optional auch **anlegen**. Dafür nutzt Du **JAX-RS** (Jakarta REST), lässt die
JSON-Umwandlung automatisch über **JSON-B** erledigen und testest die Endpunkte
mit **curl** und **REST-Assured**.

## 🎯 Lernziele

* Du verstehst, was eine REST-API ausmacht: Ressourcen als URLs, HTTP-Methoden für Operationen, Statuscodes als Ergebnis.
* Du kannst mit JAX-RS eine `Application`-Klasse und Ressourcen-Klassen erstellen und Objekte automatisch als JSON ausliefern.
* Du kannst passende HTTP-Methoden und Statuscodes für die Operationen wählen (auslesen, löschen, anlegen).
* Du kannst eine HTTP-API mit `curl` und mit REST-Assured testen.

## ✅ Definition of Done

* [ ] Das JAX-RS-Feature ist in Liberty aktiv und es existiert eine `Application`-Klasse mit dem Basispfad `/api`.
* [ ] `GET /api/todos` liefert alle Todos als JSON.
* [ ] `GET /api/todos/{id}` liefert ein einzelnes Todo als JSON – bzw. `404`, wenn es das Todo nicht gibt.
* [ ] `DELETE /api/todos/{id}` löscht ein Todo.
* [ ] *(optional)* `POST /api/todos` legt ein neues Todo an und liefert `201 Created`.
* [ ] Für die Endpunkte existieren REST-Assured-Tests, die grün laufen.
* [ ] Ihr habt die Reflexionsfragen schriftlich beantwortet.

## 🪜 Arbeitsschritte

### Teil 1: JAX-RS aktivieren

1. **Feature in der `server.xml` ergänzen:**
   ```xml
   <feature>jaxrs-2.1</feature>
   ```
   > `jaxrs-2.1` bringt auch JSON-B (`jsonb-1.0`) mit – damit werden Deine Objekte automatisch von/zu JSON umgewandelt.
2. **`Application`-Klasse anlegen**, die den Basispfad der API auf `/api` setzt (ohne sie werden die Ressourcen nicht registriert):
   ```java
   package de.schulung.jakartaee.todos;

   import javax.ws.rs.ApplicationPath;
   import javax.ws.rs.core.Application;

   @ApplicationPath("/api")
   public class RestApplication extends Application {
   }
   ```
   Alle Ressourcen liegen damit unter `.../todos-app/api/...`.

### Teil 2: Todos auslesen

3. **Ressourcen-Klasse `TodosResource` anlegen.** Sie bündelt die Endpunkte unter dem Pfad `todos` und liefert JSON:
   ```java
   @Path("todos")
   @Produces(MediaType.APPLICATION_JSON)
   public class TodosResource {
   
       // Abhängigkeiten?
   
       @GET
       public Collection<TodoDto> findTodos() {
           // ...
       }

       @GET
       @Path("{id}")
       public TodoDto findTodo(@PathParam("id") long id) {
           // ... (Todo suchen und zurückgeben)
           // für 404: throw new NotFoundException();
       }
   }
   ```
    > Die Klasse `TodoDto` repräsentiert die Daten, die an den Client gesendet werden sollen. Sie enthält nur die notwendigen Attribute mit Gettern und Settern (POJO).

    > Die Objekte werden automatisch als JSON serialisiert. Eine eigene Serialisierung musst Du nicht schreiben.

### Teil 3: Todos löschen

4. **`DELETE`-Endpunkt** ergänzen. Ohne Rückgabewert antwortet JAX-RS mit `204 No Content`:
   ```java
   @DELETE
   @Path("{id}")
   public void delete(@PathParam("id") long id) {
       // ... (Todo per ID löschen)
       // 404, falls nicht gefunden.
   }
   ```
   > Überlege: Warum passt gerade `DELETE` (und nicht `POST` oder `GET`) zu dieser Operation?

### Teil 4: Todos anlegen (optional)

5**`POST`-Endpunkt** ergänzen, der ein Todo aus dem JSON-Body entgegennimmt, anlegt und `201 Created` samt `Location`-Header der neuen Ressource zurückgibt:
   ```java
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   public Response create(TodoDto todo, @Context UriInfo uriInfo) {
       // ... (Todo anlegen/speichern)
       // Response:
       // - 201 Created
       // - Location-Header: "/api/todos/{id}"
       // - Todo mit ID im Body 
       return Response
                 .created(/* URI */)
                 .entity(responseTodo)
                 .build();
   }
   ```

### Teil 5: Testen mit curl (Git Bash)

6.**Endpunkte mit `curl` ausprobieren** (Kontextpfad `/todos-app`, API-Basis `/api`):
   ```bash
   # alle Todos
   curl -i http://localhost:9080/todos-app/api/todos

   # ein einzelnes Todo (hier id = 1)
   curl -i http://localhost:9080/todos-app/api/todos/1

   # nicht vorhandenes Todo -> 404
   curl -i http://localhost:9080/todos-app/api/todos/9999

   # ein Todo löschen -> 204
   curl -i -X DELETE http://localhost:9080/todos-app/api/todos/1

   # (optional) ein Todo anlegen -> 201
   curl -i -X POST http://localhost:9080/todos-app/api/todos \
     -H "Content-Type: application/json" \
     -d '{"title":"Neues Todo","description":"per REST angelegt","dueDate":"2026-07-20"}'
   ```
   > `-i` zeigt die Antwort inklusive Statuszeile und Header (z.B. den `Location`-Header beim Anlegen).

### Teil 6: Testen mit REST-Assured

7. **Integrationstests schreiben** (REST-Assured ist im Projekt bereits konfiguriert). Prüfe mindestens:
   - `GET /api/todos` → Status `200`, Content-Type JSON,
   - `GET /api/todos/{id}` für ein vorhandenes Todo → `200` und der erwartete Titel im JSON,
   - `GET /api/todos/{id}` für eine unbekannte `id` → `404`,
   - `DELETE /api/todos/{id}` → `204`, danach liefert `GET` derselben `id` ein `404`.
   ```java
   @BeforeAll
   static void setup() {
       RestAssured.baseURI = "http://localhost";
       RestAssured.port = Integer.getInteger("liberty.http.port", 9080);
       RestAssured.basePath = "/todos-app/api";
   }

   @Test
   void listReturnsJson() {
       given()
           .when().get("/todos")
           .then().statusCode(200).contentType(ContentType.JSON);
   }
   ```

### Teil 7: Swagger UI (optional)

8. **MicroProfile OpenAPI aktivieren**, um die API im Browser zu erkunden und zu testen:
   ```xml
   <feature>mpOpenAPI-2.0</feature>
   ```
   Danach findest Du unter [http://localhost:9080/openapi/ui](http://localhost:9080/openapi/ui) eine **Swagger UI**, die Deine Endpunkte automatisch auflistet und ausprobieren lässt. 
9. Beantwortet gemeinsam die Reflexionsfragen.

## 📚 Selbstlernmaterial

* [Jakarta REST (JAX-RS): Specification](https://jakarta.ee/specifications/restful-ws/) — die JAX-RS-Spezifikation
* [Open Liberty: Creating a RESTful web service (Guide)](https://openliberty.io/guides/rest-intro.html) — JAX-RS in Liberty end-to-end
* [Baeldung: JAX-RS Basics](https://www.baeldung.com/jax-rs-spec-and-implementations) — Überblick über JAX-RS
* [Jakarta JSON Binding (JSON-B)](https://jakarta.ee/specifications/jsonb/) — automatische JSON-Serialisierung
* [Open Liberty: Documenting RESTful APIs (MicroProfile OpenAPI)](https://openliberty.io/guides/microprofile-openapi.html) — Swagger UI und OpenAPI
* [MDN: HTTP request methods](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods) — Semantik von GET, POST, PUT, PATCH, DELETE

## 🤔 Reflexionsfragen

* Was ist der Unterschied zwischen den Statuscodes `200`, `201` und `204`?
* Könnten wir die REST-API auch ohne JAX-RS, also rein mit Servlets implementieren?
