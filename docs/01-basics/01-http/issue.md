---
layout: default
title: HTTP verstehen
---

# HTTP verstehen

HTTP (Hypertext Transfer Protocol) ist das Kommunikationsprotokoll des Webs. Jedes Mal, wenn Du eine Webseite aufrufst, sendet Dein Browser HTTP-Requests an einen Server und empfängt HTTP-Responses zurück. In dieser Übung beobachtest Du diesen Ablauf mit den Browser-DevTools.

## 🎯 Lernziele

* Du verstehst, wie die Kommunikation zwischen Browser und Server über HTTP funktioniert.
* Du kannst die Browser-DevTools (Netzwerk-Tab) nutzen, um HTTP-Requests und -Responses zu beobachten.
* Du kennst den Aufbau einer HTTP-Nachricht: Header (Metadaten) und Body (Nutzlast).
* Du kennst die wichtigsten Request-Methoden (GET, POST) und deren Unterschied.
* Du kannst HTTP-Status-Codes grob einordnen (2xx, 3xx, 4xx, 5xx).

## ✅ Definition of Done

* [ ] Du hast die Browser-DevTools geöffnet und den Netzwerk-Tab beim Laden einer Webseite beobachtet.
* [ ] Du hast einen einzelnen Request/Response genauer untersucht (Header, Body, Methode, Status-Code).
* [ ] Ihr habt die Reflexionsfragen schriftlich beantwortet.

## 🪜 Arbeitsschritte

1. Öffne einen Browser (Chrome, Firefox oder Edge) und öffne die **Developer Tools** (F12 oder Rechtsklick → "Untersuchen").
2. Wechsle zum Reiter **"Netzwerk"** (engl. "Network").
3. Lade eine beliebige Webseite (z.B. `https://www.example.com` oder eine andere Seite Deiner Wahl). Beobachte, wie im Netzwerk-Tab zahlreiche Einträge erscheinen — jeder Eintrag ist ein HTTP-Request mit zugehöriger Response.
4. Klicke auf eine Zeile (z.B. den ersten Eintrag), um die Details zu sehen. Untersuche:
   - **Request-Methode** (z.B. GET) — welche Methode wurde verwendet?
   - **Status-Code** (z.B. 200) — was bedeutet er?
   - **Request-Header** und **Response-Header** — welche Header erkennst Du? Was könnten sie bedeuten?
   - **Response-Body** (Reiter "Antwort" / "Response") — was wurde übertragen?
   - **Content-Type** — in welchem Format liegt die Antwort vor?
5. Beantwortet gemeinsam die Reflexionsfragen.

## 📚 Selbstlernmaterial

* [MDN: HTTP-Überblick](https://developer.mozilla.org/de/docs/Web/HTTP/Overview) — Einführung in HTTP
* [MDN: HTTP-Anfragemethoden](https://developer.mozilla.org/de/docs/Web/HTTP/Methods) — GET, POST und weitere Methoden
* [MDN: HTTP-Statuscodes](https://developer.mozilla.org/de/docs/Web/HTTP/Status) — Übersicht aller Status-Codes
* [MDN: HTTP-Header](https://developer.mozilla.org/de/docs/Web/HTTP/Headers) — Übersicht aller Header
* [MDN: Content-Type](https://developer.mozilla.org/de/docs/Web/HTTP/Headers/Content-Type) — Der Content-Type-Header im Detail

## 🤔 Reflexionsfragen

* Was ist "Hypertext"?
* HTTP-Nachrichten (Request und Response) bestehen aus Body und Headern. Was wird über den Body übertragen? Wofür werden Header verwendet?
* Teil des Requests ist die "Request Method". Wofür werden diese verwendet, und was ist der Unterschied zwischen "GET" und "POST"?
* Teil der Response ist der "Status Code". Wofür werden diese verwendet?
* Wofür wird der Header "Content-Type" verwendet?
