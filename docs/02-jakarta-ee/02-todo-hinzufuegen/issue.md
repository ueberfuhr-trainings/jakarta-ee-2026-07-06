---
layout: default
title: Servlets – Ein Todo über ein Formular hinzufügen
---

# Servlets – Ein Todo über ein Formular hinzufügen

In der vorigen Übung hast Du die vorhandenen Todos ausgegeben. Jetzt erweiterst
Du die Anwendung um das **Anlegen** neuer Todos: Der Benutzer füllt ein
HTML-Formular aus, ein Servlet liest die Eingaben aus, erzeugt daraus ein neues
Todo und übergibt es dem Service. Anschließend soll das neue Todo bei der
Ausgabe der Todos erscheinen.

## 🎯 Lernziele

* Du kannst ein HTML-Formular an ein Servlet senden.
* Du kannst im Servlet die Formular-Parameter auslesen und daraus ein Objekt erzeugen.
* Du kannst Zustand (die Todo-Liste) über mehrere Servlets hinweg gemeinsam nutzen.

## ✅ Definition of Done

* [ ] Es gibt ein HTML-Formular mit Eingabefeldern für Titel, Beschreibung und Frist.
* [ ] Ein Servlet liest die Formulardaten aus, erzeugt ein Todo und fügt es dem Service hinzu.
* [ ] Das hinzugefügte Todo wird beim Aufruf von `/todos` angezeigt.
* [ ] *(Optional)* Die Eingaben werden validiert; bei ungültigen Daten wird kein Todo angelegt und der Benutzer erhält eine verständliche Rückmeldung.
* [ ] Ihr habt die Reflexionsfragen schriftlich beantwortet.

## 🪜 Arbeitsschritte

1. Erstelle ein **HTML-Formular** (in der `index.html` oder einer eigenen Seite) mit Eingabefeldern für:
   - Titel
   - Beschreibung
   - Frist (Tipp: `<input type="date">`)

   Das Formular soll die Daten an die URL des neuen Servlets senden.
2. Ergänze im Service eine Methode zum **Hinzufügen** eines Todos (z. B. `addTodo(Todo todo)`).
3. Erstelle ein neues **Servlet** (z. B. `AddTodoServlet`) mit eigener URL:
   - Lies die Formular-Parameter mit `req.getParameter(...)` aus.
   - Wandle die Frist in ein `LocalDate` um (Tipp: `LocalDate.parse(...)` verarbeitet das Format `yyyy-MM-dd`, das ein `<input type="date">` liefert).
   - Erzeuge ein neues `Todo` und füge es über den Service hinzu.
   - Generiere eine passende Response.
4. Baue und starte die Anwendung mit `mvn liberty:dev` und **teste**:
   - Sende das Formular ab.
   - Rufe anschließend [http://localhost:9080/todos-app/todos](http://localhost:9080/todos-app/todos) auf und prüfe, ob Dein neues Todo dort erscheint.
5. **(Optional) Validierung.** Überlege Dir, was passiert, wenn der Benutzer unsinnige oder unvollständige Daten schickt. Prüfe die Eingaben, bevor Du ein Todo anlegst, und lehne ungültige Eingaben mit einer verständlichen Rückmeldung ab. Fordere mindestens folgende Regeln:
   - Der **Titel** muss angegeben sein und **3 bis 100 Zeichen** lang sein.
   - Die **Frist** (falls angegeben) muss **heute oder in der Zukunft** liegen.
6. **(Optional) Vorbelegung der Frist.** Sorge dafür, dass das Frist-Feld beim Laden der Seite bereits auf ein Datum **zwei Wochen in der Zukunft** vorbelegt ist.

## 📚 Selbstlernmaterial

* [Jakarta Servlet: Specification](https://jakarta.ee/specifications/servlet/) — offizielle Spezifikation der Servlet-API
* [Baeldung: Handling HTML form data with Servlets](https://www.baeldung.com/java-servlet-jsp-form) — Formulardaten in Servlets auslesen
* [MDN: Das HTML-`<form>`-Element](https://developer.mozilla.org/de/docs/Web/HTML/Element/form) — Aufbau von Formularen, `method` und `action`
* [MDN: HTTP-Methode POST](https://developer.mozilla.org/de/docs/Web/HTTP/Methods/POST) — Wofür `POST` verwendet wird
* [Java API: LocalDate.parse](https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html#parse-java.lang.CharSequence-) — Parsen eines Datums im ISO-Format

## 🤔 Reflexionsfragen

* Welche HTTP-Methode hast Du zum Anlegen des Todos verwendet, und wie begründest Du Deine Entscheidung?
* Du hast gerade ein Todo angelegt und siehst die Rückmeldung des Servlets im Browser. Was passiert, wenn Du nun **F5** drückst (die Seite neu lädst)? Welches Problem ergibt sich daraus?
