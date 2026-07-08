---
layout: default
title: JSPs – Die Todos als View darstellen
---

# JSPs – Die Todos als View darstellen

Bisher hat das Servlet die Todos selbst als Text zusammengebaut und
geschrieben. Das vermischt zwei Aufgaben: das Beschaffen der Daten und deren
Darstellung. In dieser Übung trennst Du beides: Das Servlet bereitet nur noch
die Daten auf und leitet per **Forward** an eine **JSP** weiter, die für die
Darstellung (die „View") zuständig ist. Eine JSP ist dabei nichts anderes als
ein als Text-/HTML-Template geschriebenes Servlet.

## 🎯 Lernziele

* Du verstehst, dass eine JSP letztlich in ein Servlet übersetzt wird und als HTML-Template mit dynamischen Anteilen geschrieben wird.
* Du kennst die Trennung der Verantwortlichkeiten: Das Servlet beschafft und bereitet Daten auf, die JSP stellt sie dar.
* Du kannst Daten in einem Servlet als Request-Attribut ablegen und per Forward an eine JSP weiterleiten.
* Du kannst in einer JSP Werte über die Expression Language ausgeben und mit der JSTL (`<c:forEach>`) über eine Collection iterieren.

## ✅ Definition of Done

* [ ] Die Todos werden über eine JSP dargestellt; das Servlet erzeugt die Ausgabe nicht mehr selbst, sondern leitet per Forward weiter.
* [ ] Das Servlet stellt die Todo-Collection als Request-Attribut bereit.
* [ ] Ihr habt die Reflexionsfragen schriftlich beantwortet.

## 🪜 Arbeitsschritte

1. Lege im Webapp-Verzeichnis eine **JSP** für die Anzeige der Todos an.

   > Hinweis: In der Liberty-Konfiguration muss das **JSP-Feature** aktiv sein (`jsp-2.3`). Das umfassende Feature `jakartaee-8.0` enthält es bereits – hast Du in einer früheren Übung jedoch auf `servlet-4.0` reduziert, ergänze `<feature>jsp-2.3</feature>`. Die JSTL ist im JSP-Feature enthalten und benötigt **kein** eigenes Feature.
2. Passe das Servlet, das bisher die Todos ausgibt, so an, dass es die Ausgabe **nicht mehr selbst** erzeugt, sondern:
   - die Todo-Collection als **Request-Attribut** ablegt (Tipp: `req.setAttribute("todos", ...)`),
   - per **Forward** an die JSP weiterleitet (Tipp: `req.getRequestDispatcher(...).forward(req, resp)`).
3. **Erste Ausgabe – Collection direkt:** Gib in der JSP die übergebene Collection zunächst unverändert aus (Tipp: Expression Language, z. B. `${todos}`). Baue und starte die Anwendung mit `mvn liberty:dev` und rufe [http://localhost:9080/todos-app/todos](http://localhost:9080/todos-app/todos) auf – es sollte die (per `toString()` dargestellte) Collection erscheinen.
4. **Zweite Ausgabe – mit JSTL:** Stelle die Todos nun sauber als HTML dar (z. B. als Liste oder Tabelle):
   - Nimm bei Bedarf eine **JSTL-Dependency** in die `pom.xml` auf, damit die Tags in der Entwicklungsumgebung aufgelöst werden. Wichtig: Das reine API-Artefakt enthält **keine** TLDs (Tag Library Descriptors) – und ohne TLD kann die IDE die `<c:...>`-Tags nicht auflösen. Nimm daher die **Implementierung** (GlassFish), die die TLDs mitbringt. Als `provided`, da die Laufzeit (Open Liberty) die JSTL selbst bereitstellt:
     ```xml
     <dependency>
         <groupId>org.glassfish.web</groupId>
         <artifactId>jakarta.servlet.jsp.jstl</artifactId>
         <version>1.2.6</version>
         <scope>provided</scope>
     </dependency>
     ```
   - Binde die Core-Taglib oben in der JSP ein:
     ```jsp
     <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
     ```
     > Hinweis: In Jakarta EE 8 (Namespace `javax.*`) lautet die URI `http://java.sun.com/jsp/jstl/core`.
   - Iteriere mit `<c:forEach>` über die Collection und gib pro Todo die einzelnen Attribute aus.
5. **Teste** erneut über [http://localhost:9080/todos-app/todos](http://localhost:9080/todos-app/todos) und prüfe, dass die Todos jetzt als formatiertes HTML erscheinen.

## 📚 Selbstlernmaterial

* [Jakarta Server Pages: Specification](https://jakarta.ee/specifications/pages/) — offizielle Spezifikation der JSP
* [Baeldung: Introduction to JSP](https://www.baeldung.com/jsp) — Einstieg in JSPs
* [Baeldung: JSTL](https://www.baeldung.com/jstl) — Überblick über die JSP Standard Tag Library
* [Jakarta Expression Language: Specification](https://jakarta.ee/specifications/expression-language/) — die Expression Language (`${...}`) im Detail

## 🤔 Reflexionsfragen

* Was passiert technisch mit einer JSP, bevor sie zum ersten Mal ausgeliefert wird? In welchem Verhältnis stehen JSP und Servlet zueinander?
* Welche Verantwortlichkeiten hat nach dieser Übung das Servlet, welche die JSP? Warum ist diese Trennung sinnvoll?
* Wie gelangt die Todo-Collection vom Servlet in die JSP?
* Worin unterscheidet sich die direkte Ausgabe der Collection von der Darstellung mit `<c:forEach>`? Welche Variante würdest Du in einer echten Anwendung wählen und warum?
* Was passiert, wenn Du beim Anlegen eines Todos Sonderzeichen (v.a. `<`, `>`) verwendest?
