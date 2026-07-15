---
layout: default
title: REST API – Todos anlegen (POST)
---

# REST API – Todos anlegen (POST)

Bisher spricht die Anwendung mit dem Browser über Servlets und JSPs – die
Antwort ist fertiges HTML. Moderne Clients (z.B. Single Page Applications oder
mobile Apps) wollen aber lieber **Daten** (JSON) statt HTML und sprechen den
Server über eine **REST-API** an. In dieser Übung baust Du den ersten Endpunkt:
das **Anlegen** eines Todos per `POST`. Dafür nutzt Du **JAX-RS** (Jakarta REST)
und lässt die JSON-Umwandlung automatisch über **JSON-B** erledigen.

Der `POST`-Endpunkt und der `TodosService` sind als **Vorlage vorgegeben**;
selbst schreiben musst Du das **`TodoDto`** und den **Mapper** zwischen `Todo`
und `TodoDto`. Zum Prüfen legst Du ein Todo per REST an und schaust in der
**klassischen UI** (`/todos-app/todos`) nach, ob es dort erscheint.

> Die weiteren Endpunkte (Todos auslesen und löschen) folgen in der nächsten
> Übung.

## 🎯 Lernziele

* Du verstehst, was eine REST-API ausmacht: Ressourcen als URLs, HTTP-Methoden für Operationen, Statuscodes als Ergebnis.
* Du kannst mit JAX-RS eine `Application`-Klasse und eine Ressourcen-Klasse erstellen und Objekte automatisch als JSON austauschen.
* Du kannst ein DTO mit Mapper einsetzen, um die interne Datenstruktur nicht direkt nach außen zu geben.
* Du kannst die Eingabe per Bean Validation (`@Valid`) am Endpunkt prüfen lassen.

## ✅ Definition of Done

* [ ] Das JAX-RS-Feature ist in Liberty aktiv und es existiert eine `Application`-Klasse mit dem Basispfad `/api`.
* [ ] Du hast ein `TodoDto` (mit read-only `id`) und einen Mapper (`Todo` ↔ `TodoDto`) geschrieben.
* [ ] `POST /api/todos` legt ein Todo an und liefert `201 Created` mit `Location`-Header und dem angelegten Todo im Body.
* [ ] Ungültige Eingaben werden per `@Valid` mit `400` abgelehnt.
* [ ] Das per REST angelegte Todo erscheint in der klassischen UI unter `/todos-app/todos`.
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
   package de.schulung.jakartaee.todos.boundary;

   import javax.ws.rs.ApplicationPath;
   import javax.ws.rs.core.Application;

   @ApplicationPath("/api")
   public class RestApplication extends Application {
   }
   ```
   Alle Ressourcen liegen damit unter `.../todos-app/api/...`.

### Teil 2: Vorlage – Anlegen (`POST`)

Der `POST`-Endpunkt ist **vorgegeben**. Studiere ihn – er zeigt Dir das Muster
(Ressource, HTTP-Methode, Validierung, Mapper-Nutzung). Den `TodosService` für
den Datenzugriff gibt es im Projekt bereits.

3. **`TodosResource` mit `POST`-Endpunkt** (vorgegeben):
   ```java
   @Path("todos")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public class TodosResource {

       @Inject
       TodosService todosService;
       @Inject
       TodoMapper mapper;          // <-- schreibst Du in Teil 3

       @POST
       public Response create(@Valid TodoDto dto, @Context UriInfo uriInfo) {
           Todo created = todosService.addTodo(mapper.toDomain(dto));
           URI location = uriInfo.getAbsolutePathBuilder()
                   .path(String.valueOf(created.getId()))
                   .build();
           return Response.created(location)
                   .entity(mapper.toDto(created))
                   .build();
       }
   }
   ```
   > `@Valid` am Parameter sorgt dafür, dass das `TodoDto` vor dem Methodenrumpf validiert wird; bei Verstößen antwortet JAX-RS automatisch mit `400`. Die Validierungsconstraints kannst Du direkt an das `TodoDto` schreiben (siehe Teil 3).

**Was der Endpunkt auf HTTP-Ebene leisten muss** (Informationsmaterial): Ein
Client legt ein Todo an, indem er die Daten als JSON per `POST` an die
Sammel-Ressource `/api/todos` schickt. Der Server legt das Todo an und antwortet
mit `201 Created`, einem `Location`-Header auf die neue Einzel-Ressource und dem
angelegten Todo (jetzt mit `id`) im Body.

*Anfrage:*
```http
POST /todos-app/api/todos HTTP/1.1
Host: localhost:9080
Content-Type: application/json

{
  "title": "Einkaufen gehen",
  "description": "Milch, Brot und Butter besorgen",
  "dueDate": "2026-07-20"
}
```

*Antwort:*
```http
HTTP/1.1 201 Created
Location: http://localhost:9080/todos-app/api/todos/42
Content-Type: application/json

{
  "id": 42,
  "title": "Einkaufen gehen",
  "description": "Milch, Brot und Butter besorgen",
  "dueDate": "2026-07-20",
  "status": "ready"
}
```

Merkmale, die die Vorlage umsetzt:
* **Methode & Pfad:** `POST` auf die Sammel-Ressource `/api/todos` (nicht auf eine Einzel-Ressource – die `id` entsteht ja erst beim Anlegen).
* **Anfrage-Body:** das zu erstellende Todo als JSON; `Content-Type: application/json`. Die `id` wird **nicht** mitgeschickt.
* **Status:** `201 Created` (nicht `200`), weil eine neue Ressource entstanden ist.
* **`Location`-Header:** die URL der neu angelegten Einzel-Ressource, unter der sie später per `GET` abrufbar sein wird.
* **Antwort-Body:** das angelegte Todo inklusive der vom Server vergebenen `id`.

### Teil 3: `TodoDto` und Mapper selbst schreiben

4. **`TodoDto`** anlegen – ein einfaches POJO mit den Attributen, die über die API übertragen werden (z.B. `id`, `title`, `description`, `dueDate`, `status`), mit Gettern und Settern. So gibst Du nicht die interne `Todo`-Klasse direkt nach außen.
   - **Validierung:** Die Constraints darfst Du direkt an das `TodoDto` schreiben (z.B. `@NotNull`/`@Title` am `title`). Zusammen mit `@Valid` am Endpunkt (Teil 2) werden ungültige Anfragen mit `400` abgelehnt.
   - **`id` ist read-only:** Sie erscheint in den Antworten, wird beim Anlegen aber **nicht** vom Client mitgeschickt. In JSON-B erreichst Du das, indem Du **den Setter** von `id` mit `@JsonbTransient` annotierst:
     ```java
     private Long id;

     public Long getId() {        // Getter ohne Annotation -> id wird serialisiert (Antwort)
         return id;
     }

     @JsonbTransient              // nur der Setter -> id wird beim Deserialisieren (Anfrage) ignoriert
     public void setId(Long id) {
         this.id = id;
     }
     ```
     > Wichtig: `@JsonbTransient` gehört an den **Setter**, nicht an das Feld – am Feld würde die `id` aus **beiden** Richtungen verschwinden (also auch aus der Antwort). Der Mapper kann `setId(...)` als normalen Java-Aufruf weiter nutzen; `@JsonbTransient` wirkt nur auf JSON-B.
5. **Mapper** anlegen, der beide Richtungen beherrscht (die Vorlage braucht beide):
   - `Todo` → `TodoDto` (für die Ausgabe),
   - `TodoDto` → `Todo` (für das Anlegen).
   > Ob Du den Mapper als CDI-Bean (`@ApplicationScoped`, injizierbar) oder als einfache Klasse baust, bleibt Dir überlassen – die Vorlage injiziert ihn per `@Inject`.

### Teil 4: Prüfen über die klassische UI

6. **Anlegen per REST und in der UI prüfen.** Lege ein Todo per `POST` an (z.B. mit `curl` in der Git Bash) und öffne anschließend die bestehende Anzeige unter [http://localhost:9080/todos-app/todos](http://localhost:9080/todos-app/todos) – das neue Todo muss dort auftauchen.
   ```bash
   # anlegen -> 201 mit Location-Header
   curl -i -X POST http://localhost:9080/todos-app/api/todos \
     -H "Content-Type: application/json" \
     -d '{"title":"Neues Todo","description":"per REST angelegt","dueDate":"2026-07-20"}'

   # ungültige Eingabe (zu kurzer Titel) -> 400
   curl -i -X POST http://localhost:9080/todos-app/api/todos \
     -H "Content-Type: application/json" \
     -d '{"title":"Ab"}'
   ```
   > `-i` zeigt die Antwort inklusive Statuszeile und Header (z.B. den `Location`-Header beim Anlegen).

### Teil 5: Swagger UI (optional)

7. **MicroProfile OpenAPI aktivieren**, um die API im Browser zu erkunden und zu testen:
   ```xml
   <feature>mpOpenAPI-2.0</feature>
   ```
   Danach findest Du unter [http://localhost:9080/openapi/ui](http://localhost:9080/openapi/ui) eine **Swagger UI**, die Deine Endpunkte automatisch auflistet und ausprobieren lässt. Schau Dir dabei über die Developer Tools im Browser auch die Requests und Responses an.

### Teil 6 (optional): Todo-Status-Werte über JSON steuern

8. Der interne `TodoStatus` heißt `ERSTELLT` / `IN_ARBEIT` / `FERTIG`. Über die API sollen aber die Werte **`ready`**, **`in_progress`** und **`done`** übertragen werden – in **beide** Richtungen (Request und Response). Löse das im DTO/Mapper, z.B.:
   - `status` im `TodoDto` als `String` führen und im Mapper beide Richtungen übersetzen (`"ready"` ↔ `ERSTELLT`, `"in_progress"` ↔ `IN_ARBEIT`, `"done"` ↔ `FERTIG`),
   - unbekannte/leere Werte sinnvoll behandeln (z.B. Default `ready`).
   - Validierung soll nur diese 3 Werte erlauben
9. Beantwortet gemeinsam die Reflexionsfragen.

## 📚 Selbstlernmaterial

* [Jakarta REST (JAX-RS): Specification](https://jakarta.ee/specifications/restful-ws/) — die JAX-RS-Spezifikation
* [Open Liberty: Creating a RESTful web service (Guide)](https://openliberty.io/guides/rest-intro.html) — JAX-RS in Liberty end-to-end
* [Jakarta JSON Binding (JSON-B)](https://jakarta.ee/specifications/jsonb/) — automatische JSON-Serialisierung
* [Sebastian Daschner: JSON-B Asymmetrical Property Binding](https://blog.sebastian-daschner.com/entries/jsonb-asymmetrical-property-binding) — read-only/write-only mit `@JsonbTransient`
* [Baeldung: Bean Validation in JAX-RS](https://www.baeldung.com/jersey-bean-validation) — `@Valid` an Endpunkten
* [Open Liberty: Documenting RESTful APIs (MicroProfile OpenAPI)](https://openliberty.io/guides/microprofile-openapi.html) — Swagger UI und OpenAPI
* [MDN: HTTP request methods](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods) — Semantik von GET, POST, PUT, PATCH, DELETE

## 🤔 Reflexionsfragen

* Warum antwortet das Anlegen mit `201 Created` und nicht mit `200 OK`? Wozu dient der `Location`-Header?
* Warum geben wir nicht direkt die interne `Todo`-Klasse nach außen, sondern ein eigenes `TodoDto`?
* Warum ist es sinnvoll, die `id` als read-only zu behandeln? Was könnte passieren, wenn ein Client sie beim Anlegen mitschicken dürfte?
* Wo findet die Validierung statt, und was passiert bei einem Verstoß (Statuscode)?
