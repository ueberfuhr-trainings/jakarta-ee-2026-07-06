---
layout: default
title: Servlets – Todos als Plain Text ausgeben
---

# Servlets – Todos als Plain Text ausgeben

Ein Servlet ist die klassische Jakarta-EE-Komponente, um HTTP-Requests
serverseitig zu verarbeiten und Responses zu erzeugen. In dieser Übung baust Du
den ersten Baustein einer kleinen Todo-Anwendung: ein Domänenmodell, eine
einfache Verwaltung mit Beispieldaten und ein Servlet, das die vorhandenen Todos
als reinen Text (`text/plain`) ausgibt. In den folgenden Übungen wird die
Anwendung schrittweise um das Erstellen und Suchen von Todos erweitert.

## 🎯 Lernziele

* Du verstehst, was ein Servlet ist und wie es einen HTTP-GET-Request verarbeitet.
* Du kannst ein Servlet mit `@WebServlet` registrieren und in `doGet(...)` eine Response mit passendem `Content-Type` erzeugen.
* Du kannst einen Request-Parameter mit `getParameter(...)` auslesen und damit die Ausgabe filtern.
* Du kennst den Unterschied zwischen dem Namespace `javax.*` (Jakarta EE 8) und `jakarta.*` (ab Jakarta EE 9).

## ✅ Definition of Done

* [ ] Ein Servlet gibt die Todos als `text/plain` aus und ist im Browser erreichbar.
* [ ] Das Servlet filtert bei gesetztem Parameter `search` die Todos case-insensitive nach im Titel enthaltenem Suchtext.
* [ ] *(Optional)* In der Liberty-Konfiguration ist `jakartaee-8.0` auskommentiert und nur das Servlet-Feature aktiv.
* [ ] *(Optional)* Unter `src/main/webapp` gibt es eine `index.html` als Startseite mit einem Link zum Servlet – optional mit Suchformular.
* [ ] Ihr habt die Reflexionsfragen schriftlich beantwortet.

## 🪜 Arbeitsschritte

> Ausnahmsweise sind die Arbeitsschritte in drei Teile gegliedert: Zuerst gibst Du
> alle Todos aus und testest das, danach führst Du die Suche ein und testest sie,
> und der dritte Teil (Startseite) ist optional.

### Teil 1: Servlet, das alle Todos ausgibt

1. Erstelle ein **POJO `Todo`** mit folgenden Attributen:
   - Titel
   - Beschreibung
   - Fälligkeitsdatum (DueDate)
   - Status mit den Ausprägungen „erstellt", „in Arbeit" und „fertig"
2. Erstelle eine **Verwaltungsklasse**, die die Todos hält und über einen Getter bereitstellt. Lege darin **zwei** Beispiel-Todos an.
3. Erstelle das **Servlet**, das zunächst **alle** Todos ausgibt:
   - Eine Klasse, die von `javax.servlet.http.HttpServlet` erbt.
   - Registriere sie mit der Annotation `@WebServlet("/todos")`.
   - Überschreibe `doGet(HttpServletRequest req, HttpServletResponse resp)`.
   - Setze den Content-Type auf `text/plain` (Tipp: `resp.setContentType("text/plain;charset=UTF-8")`).
   - Hole die Todos aus der Verwaltungsklasse und schreibe sie zeilenweise über `resp.getWriter()` in die Response.

   > Hinweis: Das Projekt nutzt **Jakarta EE 8**. Die Imports beginnen daher noch mit `javax.servlet.*` – erst ab Jakarta EE 9 heißen sie `jakarta.servlet.*`.
4. **(Optional)** Stelle in der Liberty-Konfiguration `src/main/liberty/config/server.xml` das benötigte Feature um: Kommentiere das umfassende Feature `jakartaee-8.0` aus und aktiviere stattdessen nur das Servlet-Feature. So lädt der Server gezielt nur das, was diese Übung braucht.
   ```xml
   <featureManager>
       <!-- <feature>jakartaee-8.0</feature> -->
       <feature>servlet-4.0</feature>
   </featureManager>
   ```
   > Hinweis: In Jakarta EE 8 hat die Servlet-Spezifikation die Version 4.0 – daher `servlet-4.0`.
5. Baue und starte die Anwendung mit Open Liberty und **teste** die Ausgabe:
   ```bash
   cd todos-app
   mvn liberty:dev
   ```
   Rufe im Browser [http://localhost:9080/todos-app/todos](http://localhost:9080/todos-app/todos) auf und prüfe, ob die beiden Beispiel-Todos als Text erscheinen.

### Teil 2: Suche nach dem Titel

1. Erweitere das `doGet(...)` des Servlets um einen **optionalen Suchtext-Parameter**:
   - Lies den Parameter aus dem Request (Tipp: `req.getParameter("search")`).
   - Ist er gesetzt (nicht `null` und nicht leer), gib nur die Todos aus, deren **Titel** den Suchtext enthält – **case-insensitive** (Tipp: beide Werte mit `toLowerCase()` vergleichen und `contains(...)` verwenden).
   - Ist der Parameter nicht gesetzt, werden wie bisher alle Todos ausgegeben.
2. **Teste** die Suche, indem Du einen Suchbegriff als Query-Parameter anhängst, z. B. `http://localhost:9080/todos-app/todos?search=abc`. Prüfe, dass nur passende Todos erscheinen und ohne Parameter weiterhin alle ausgegeben werden.

### Teil 3 (optional): Startseite mit `index.html`

1. Lege unter `src/main/webapp` eine Startseite `index.html` an, die auf das Servlet verlinkt, z. B.:
   ```html
   <!DOCTYPE html>
   <html lang="de">
   <head>
       <meta charset="UTF-8">
       <title>Todos</title>
   </head>
   <body>
       <h1>Todos-App</h1>
       <p><a href="todos">Alle Todos anzeigen</a></p>
   </body>
   </html>
   ```
   Der relative Link `todos` führt zusammen mit dem Context-Root `/todos-app` zum Servlet.
2. Ergänze in der `index.html` ein Suchformular, das den Suchbegriff per GET als Parameter `search` an das Servlet übergibt:
   ```html
   <form action="todos" method="get">
       <input type="text" name="search" placeholder="Titel durchsuchen…">
       <button type="submit">Suchen</button>
   </form>
   ```
   Beim Absenden ruft der Browser z. B. `todos?search=…` auf – genau den Parameter, den das Servlet ausliest.
3. **Teste** die Startseite: Öffne [http://localhost:9080/todos-app/](http://localhost:9080/todos-app/), folge dem Link und nutze das Suchformular.

### Abschluss

Beantwortet gemeinsam die Reflexionsfragen.

## 📚 Selbstlernmaterial

* [Jakarta Servlet: Specification](https://jakarta.ee/specifications/servlet/) — offizielle Spezifikation der Servlet-API
* [Baeldung: Intro to Servlets](https://www.baeldung.com/intro-to-servlets) — Einstieg in Servlets und den Request-/Response-Zyklus
* [Open Liberty: Dev mode](https://openliberty.io/docs/latest/development-mode.html) — Entwicklung mit `mvn liberty:dev`
* [MDN: Content-Type](https://developer.mozilla.org/de/docs/Web/HTTP/Headers/Content-Type) — der Content-Type-Header, hier `text/plain`
* [Jakarta EE 8 vs. 9 – Namespace-Wechsel](https://jakarta.ee/blogs/javax-jakartaee-namespace-ecosystem-progress/) — Hintergrund zum Wechsel von `javax` zu `jakarta`

## 🤔 Reflexionsfragen

* Was ist ein Servlet, und welche Rolle spielt es innerhalb eines Web-Containers wie Open Liberty?
* Wofür steht das Mapping in `@WebServlet("/todos")`, und wie ergibt sich daraus die aufgerufene URL zusammen mit dem Context-Root `/todos-app`?
* Warum wird der `Content-Type` der Response gesetzt, und was würde passieren, wenn Du ihn weglässt oder auf `text/html` setzt?
* Die Verwaltungsklasse hält die Todos aktuell nur im Arbeitsspeicher. Welche Konsequenz hat das, wenn der Server neu gestartet wird?
* Warum beginnen die Servlet-Imports in diesem Projekt mit `javax.` und nicht mit `jakarta.`?
* Wie gelangt der Suchbegriff aus dem Formular bzw. der URL in das Servlet, und was liefert `getParameter("search")` zurück, wenn der Parameter gar nicht angegeben wurde?
