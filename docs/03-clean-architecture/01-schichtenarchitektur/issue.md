---
layout: default
title: Schichtenarchitektur
---

# Schichtenarchitektur

Bisher liegen alle Klassen der Todos-Anwendung in einem einzigen Package. Das
funktioniert, aber es verrät nicht, welche Klasse welche Aufgabe hat, und es
lässt zu, dass alles von allem abhängt. In dieser Übung führst Du eine
**Schichtenarchitektur** ein: Du trennst die Anwendung in **boundary** (Web/HTTP),
**domain** (Fachlogik) und **persistence** (Datenzugriff). Anschließend sorgst
Du dafür, dass die Abhängigkeiten in die **richtige Richtung** zeigen – die
Fachlogik soll nicht von der Datenbanktechnik abhängen, sondern umgekehrt
(**Dependency Inversion**). Ziel ist nicht möglichst viel Code, sondern ein
Gespür dafür, *warum* wir Schichten bilden und Abhängigkeiten kontrollieren.

## 🎯 Lernziele

* Du verstehst, warum eine Schichtenarchitektur (Trennung von Verantwortlichkeiten) die Wartbarkeit und Testbarkeit erhöht.
* Du kannst eine Anwendung in die Schichten boundary, domain und persistence aufteilen.
* Du verstehst, was die **Richtung** einer Abhängigkeit bedeutet und warum die Domäne im Zentrum stehen sollte.
* Du kannst mit einem Interface eine Abhängigkeit umdrehen (Dependency Inversion) und die konkrete Technik hinter eine Abstraktion legen.

## ✅ Definition of Done

* [ ] Die Klassen sind in die Packages `boundary`, `domain` und `persistence` aufgeteilt, die Anwendung kompiliert und läuft weiterhin.
* [ ] Der Datenzugriff steckt hinter einem Interface `TodosDao` in der Domäne, das in der Persistenzschicht implementiert wird.
* [ ] Die Domäne (`TodosService`) kennt weder `EntityManager` noch die konkrete DAO-Implementierung.
* [ ] Ihr habt die Reflexionsfragen schriftlich beantwortet.

## 🪜 Arbeitsschritte

### Teil 1: Klassen in Schichten aufteilen

1. **Drei Packages anlegen** unterhalb von `de.schulung.jakartaee.todos`:
   - `boundary` – alles, was mit der Außenwelt (HTTP/Web) spricht,
   - `domain` – das Fachmodell und die Fachlogik,
   - `persistence` – der technische Datenzugriff.
2. **Bestehende Klassen verschieben** (Tipp: die IDE passt `package`-Deklarationen und Imports automatisch an):
   - `boundary`: die Servlets (`AddTodoServlet`, `ReadTodosServlet`, `HelloWorldServlet`).
   - `domain`: `Todo`, `TodoStatus`, die Bean-Validation-Constraints (`Title`, `MaximumFuture`, `MaximumFutureValidator`), `HelloMessage`, `SampleDataInitializer` und der Business-Service `TodosService`.
   - `persistence`: bleibt vorerst **leer**.
3. **Die Entity-Referenz in `persistence.xml`** auf das neue Package anpassen (`...domain.Todo`).
4. **Kompilieren und starten**, prüfen, dass die Anwendung unverändert funktioniert.
   > Fällt Dir etwas auf? Der `TodosService` liegt in `domain`, greift aber selbst über den `EntityManager` auf die Datenbank zu – die Fachlogik hängt also direkt an der Persistenztechnik, und die `persistence`-Schicht ist leer. Dieses **offene Problem** lösen wir in Teil 2.

### Teil 2: Abhängigkeit umdrehen (Dependency Inversion)

Lies vorab den kurzen Artikel von Robert C. Martin (»Uncle Bob«):
[A Little Architecture](https://blog.cleancoder.com/uncle-bob/2016/01/04/ALittleArchitecture.html).
Die Kernidee: Die **wichtigen** Dinge (die Fachlogik) sollen nicht von den
**unwichtigen** Details (Datenbank, Framework) abhängen – sondern umgekehrt.

5. **Interface `TodosDao` in `domain` anlegen.** Es beschreibt die Datenzugriffs-Operationen, die die Domäne braucht – aus Sicht der Domäne, ohne JPA-Bezug:
   ```java
   public interface TodosDao {
       Collection<Todo> findAll();
       void save(Todo todo);
       long count();
       Collection<Todo> findByTitleContains(String search);
   }
   ```
   > Tipp: `count()` und `findByTitleContains(...)` lassen sich als `default`-Methoden auf Basis von `findAll()` vorformulieren. Eine Implementierung kann sie später bei Bedarf effizienter überschreiben.
6. **Implementierung `JpaTodosDao` in `persistence` anlegen.** Sie implementiert `TodosDao`, bekommt den `EntityManager` per `@PersistenceContext` und enthält die JPQL-Abfragen (verschoben aus dem bisherigen `TodosService`). Für effiziente `count`/`findByTitleContains` überschreibe die Defaults mit `COUNT`- bzw. `LIKE`-Abfragen.
7. **`TodosService` entkoppeln.** Der Service lässt sich das `TodosDao` injizieren und **delegiert** den Datenzugriff; die Fachlogik (z.B. die `@Valid`-Validierung beim Anlegen) bleibt hier. `EntityManager` und JPA-Importe verschwinden aus der Domäne.
8. **Kompilieren und starten**, prüfen, dass alles unverändert funktioniert.
   > Betrachte jetzt die Abhängigkeiten: `persistence` hängt von `domain` ab (es implementiert deren Interface) – nicht mehr umgekehrt. Die Domäne steht im Zentrum und kennt die Datenbanktechnik nicht.
9. Beantwortet gemeinsam die Reflexionsfragen.

## 📚 Selbstlernmaterial

* [Robert C. Martin: A Little Architecture](https://blog.cleancoder.com/uncle-bob/2016/01/04/ALittleArchitecture.html) — warum die Domäne im Zentrum steht und Details außen liegen
* [Robert C. Martin: The Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) — die bekannte „Zwiebel"-Darstellung mit der Dependency Rule
* [Baeldung: The DAO Pattern in Java](https://www.baeldung.com/java-dao-pattern) — das Data-Access-Object als Abstraktion über der Persistenz
* [Jakarta CDI: Dependency Injection](https://jakarta.ee/specifications/cdi/) — wie CDI ein Interface auf seine Implementierung verdrahtet

## 🤔 Reflexionsfragen

* Welche Vorteile bringt die Aufteilung in `boundary`/`domain`/`persistence`?
* In Teil 1 hängt die Domäne an der Persistenztechnik (`EntityManager`). Warum ist das ein Problem?
* Was genau bedeutet **Dependency Inversion**, und wie kehrt das Interface `TodosDao` die Abhängigkeitsrichtung um? Wer entscheidet über den Aufbau des Interfaces - `domain` oder `persistence`?
* Wie wichtig ist die Rolle von Dependency Injection (CDI) hier? Ginge es auch ohne?
