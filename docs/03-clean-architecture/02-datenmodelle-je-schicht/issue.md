---
layout: default
title: Eigenes Datenmodell je Schicht
---

# Eigenes Datenmodell je Schicht

In der vorigen Übung hast Du die Anwendung in Schichten aufgeteilt und die
Abhängigkeit zwischen Domäne und Persistenz umgedreht. Eine Sache teilen sich
die Schichten aber noch: **ein einziges `Todo`**, das gleichzeitig
Domänenmodell, JPA-Entity und Anzeige-Objekt ist. Diese Klasse trägt darum
Annotationen aus drei Welten (JPA, Bean Validation) und ändert sich, sobald sich
*irgendeine* Schicht ändert. In dieser Übung gibst Du **jeder Schicht ihr
eigenes Datenmodell** und verbindest sie über **handgeschriebene Mapper** – ganz
ohne Mapping-Framework. So wird sichtbar, was jede Schicht wirklich braucht, und
die Schichten können sich unabhängig voneinander entwickeln.

## 🎯 Lernziele

* Du verstehst, warum ein gemeinsames Modell über alle Schichten hinweg problematisch ist (vermischte Verantwortlichkeiten, ungewollte Kopplung).
* Du kannst je Schicht ein passendes Datenmodell entwerfen (Domänenmodell, Persistenz-Entity, Transfer-/Anzeige-DTO).
* Du kannst Modelle von Hand ineinander umwandeln (Mapper) und dabei bewusst nur die tatsächlich benötigten Richtungen umsetzen.
* Du erkennst, dass ein Modellwechsel an einer Schichtgrenze auch eine Chance zur Aufbereitung ist (z.B. Enum → Anzeige-String).

## ✅ Definition of Done

* [ ] Jede Schicht hat ihr eigenes Todo-Modell: `Todo` (domain), `TodoEntity` (persistence), `TodoJspDto` (boundary).
* [ ] Das Domänenmodell trägt keine JPA-Annotationen mehr; die Entity trägt keine fachlichen Validierungsregeln.
* [ ] Der Datenzugriff und die Anzeige laufen über Mapper; die Anwendung funktioniert unverändert.
* [ ] Ihr habt die Reflexionsfragen schriftlich beantwortet.

## 🪜 Arbeitsschritte

### Teil 1: Persistenz-Entity herauslösen

1. **`TodoEntity` in `persistence` anlegen.** Verschiebe die JPA-Annotationen (`@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`, `@Enumerated`) aus `Todo` in diese neue Klasse. Sie enthält nur Persistenz-Details, **keine** Bean-Validation-Annotationen.
2. **`Todo` (domain) zum reinen POJO machen.** Entferne die JPA-Annotationen und -Importe; die fachlichen Regeln (Bean Validation) bleiben. Ergänze bei Bedarf einen Setter für die `id` (für das Mapping).
3. **Mapper `TodoEntityMapper` in `persistence` anlegen** (von Hand, kein Framework) mit beiden Richtungen: `toEntity(Todo)` und `toDomain(TodoEntity)`.
4. **`JpaTodosDao` anpassen.** Die JPQL-Abfragen laufen jetzt über `TodoEntity`; an der Grenze zur Domäne wird über den Mapper zu/von `Todo` umgewandelt. Nach außen (Interface `TodosDao`) bleibt alles beim Domänenmodell.
5. **`persistence.xml`** auf `...persistence.TodoEntity` umstellen. Danach kompilieren und starten, prüfen, dass alles funktioniert.
   > Der `TodoStatus` (Enum) darf bewusst schichtübergreifend geteilt bleiben – ein eigenes Enum je Schicht brächte hier vor allem Mapping-Aufwand ohne fachlichen Mehrwert.

### Teil 2: Anzeige-DTO für die JSP

6. **`TodoJspDto` in `boundary` anlegen.** Es enthält nur die für die Anzeige nötigen Felder. Den **Status modellieren wir hier als `String`** – als eine bereits für die Anzeige aufbereitete Bezeichnung, nicht als Enum.
7. **Mapper `TodoJspDtoMapper` in `boundary` anlegen** – nur die Richtung `Todo → TodoJspDto` (mehr wird für die Anzeige nicht gebraucht). Bilde den `TodoStatus` per **`switch-case`** auf einen Anzeige-String ab (bewusst **nicht** über `Enum#name()`, damit die Anzeige unabhängig von den internen Enum-Namen und an einer Stelle steuerbar bleibt).
8. **`ReadTodosServlet` anpassen.** Es holt die Domänen-`Todo`s vom Service, mappt sie zu `TodoJspDto`s und gibt diese an die JSP weiter. Die JSP sieht damit nur das DTO der Web-Schicht.
   > `AddTodoServlet` darf beim Anlegen weiterhin direkt ein `Todo` erzeugen – ein DTO fürs Schreiben brauchen wir hier nicht.
9. **Kompilieren und starten**, prüfen, dass Anzeige, Anlegen und Suche unverändert funktionieren (die Status-Anzeige zeigt jetzt die aufbereiteten Bezeichnungen).
10. Beantwortet gemeinsam die Reflexionsfragen.

## 📚 Selbstlernmaterial

* [Martin Fowler: Data Transfer Object](https://martinfowler.com/eaaCatalog/dataTransferObject.html) — Zweck und Einsatz von DTOs
* [Baeldung: The DTO Pattern](https://www.baeldung.com/java-dto-pattern) — DTOs und Mapping in Java
* [Martin Fowler: Local DTO](https://martinfowler.com/bliki/LocalDTO.html) — wann sich (lokale) DTOs und das Mapping lohnen – und wann nicht
* [Robert C. Martin: A Little Architecture](https://blog.cleancoder.com/uncle-bob/2016/01/04/ALittleArchitecture.html) — warum Details (DB, UI) nicht ins Zentrum gehören

## 🤔 Reflexionsfragen

* Welche Herausforderungen hatte das eine gemeinsame `Todo` für alle Schichten?
* Warum bilden wir den Status im Mapper für die `TodoJspDto` per `switch-case` ab und nicht über `Enum#name()`?
