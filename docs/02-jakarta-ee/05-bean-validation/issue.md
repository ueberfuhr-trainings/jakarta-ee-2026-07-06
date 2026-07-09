---
layout: default
title: Bean Validation
---

# Bean Validation

In der Übung zum Anlegen eines Todos hast Du die Eingaben von Hand geprüft
(Titel-Länge, Frist in der Zukunft). Solche Prüfungen als `if`-Kaskaden zu
schreiben, ist mühsam, wiederholt sich und verteilt die Regeln über den ganzen
Code. **Bean Validation** dreht das um: Du deklarierst die Regeln als
**Annotationen** direkt am Todo, und der Container prüft sie für Dich – im
Servlet über einen injizierten `Validator`, im Service sogar automatisch beim
Methodenaufruf.

## 🎯 Lernziele

* Du verstehst, was Bean Validation ist und wie Constraints als Annotationen am Modell deklariert werden.
* Du kannst einen `Validator` per `@Inject` beziehen und ein Objekt programmatisch validieren.
* Du verstehst **Method Validation**: Constraints an Methodenparametern werden bei CDI-Beans automatisch geprüft – ganz ohne expliziten `Validator`.
* Du kannst Bean Validation in Open Liberty aktivieren.

## ✅ Definition of Done

* [ ] Das `Todo` ist mit Constraints annotiert (u. a. Titel 3–100 Zeichen, Frist heute oder in der Zukunft).
* [ ] Im Servlet wird ein per `@Inject` bezogener `Validator` genutzt; bei Verstößen wird kein Todo angelegt und der Benutzer erhält eine verständliche Rückmeldung.
* [ ] Der Service prüft seinen Methodenparameter automatisch per Method Validation (Constraint-Annotation am Parameter, **kein** expliziter `Validator`-Aufruf).
* [ ] Die von Hand geschriebenen `if`-Prüfungen aus der vorigen Übung sind entfernt.
* [ ] Das Bean-Validation-Feature ist in der Liberty-Konfiguration aktiv.
* [ ] Ihr habt die Reflexionsfragen schriftlich beantwortet.

## 🪜 Arbeitsschritte

1. **Feature aktivieren.** In der Liberty-Konfiguration muss das Feature `beanValidation-2.0` aktiv sein (und `cdi-2.0`, das Du bereits nutzt). Das umfassende Feature `jakartaee-8.0` enthält beide bereits; bei einem reduzierten Feature-Satz ergänze `<feature>beanValidation-2.0</feature>`.
2. **Das Todo annotieren.** Versieh die Felder des `Todo` mit Constraint-Annotationen aus `javax.validation.constraints.*`, u. a.:
   - Titel: nicht leer und **3–100 Zeichen** (Tipp: `@NotNull`/`@NotBlank` und `@Size(min = 3, max = 100)`).
   - Frist: falls angegeben **heute oder in der Zukunft** (Tipp: `@FutureOrPresent` – erlaubt `null`).
3. **Im Servlet mit dem `Validator` prüfen.** Lass Dir einen `javax.validation.Validator` per `@Inject` injizieren. Erzeuge aus den Formulardaten ein `Todo` und validiere es (`validator.validate(todo)`). Ist die zurückgegebene Menge der `ConstraintViolation`s nicht leer, lege **kein** Todo an, sondern gib die Verstöße verständlich aus. Entferne die alten `if`-Prüfungen.
4. **Im Service per Method Validation prüfen.** Annotiere den Parameter der Hinzufügen-Methode so, dass er validiert wird (Tipp: `@Valid` bzw. die Constraints direkt am Parameter). Da der Service ein **CDI-Bean** ist (siehe vorige Übung) und injiziert wird, prüft der Container den Parameter beim Aufruf automatisch – Du brauchst hier **keinen** `Validator`. Schlägt die Prüfung fehl, wirft der Container eine `ConstraintViolationException`.
5. **Testen.**
   - Lege ein gültiges Todo an – es soll erscheinen.
   - Sende ungültige Daten (zu kurzer Titel, Frist in der Vergangenheit) – das Servlet soll sie mit einer verständlichen Meldung ablehnen.
   - Provoziere den Fall, dass das Servlet eine Prüfung *nicht* macht, der Service sie aber erzwingt, und beobachte, was passiert.

## 📚 Selbstlernmaterial

* [Jakarta Bean Validation: Specification](https://jakarta.ee/specifications/bean-validation/) — offizielle Spezifikation
* [Baeldung: Java Bean Validation Basics](https://www.baeldung.com/javax-validation) — Constraints, `Validator`, `ConstraintViolation`
* [Baeldung: Method Constraints with Bean Validation](https://www.baeldung.com/javax-validation-method-constraints) — Validierung von Methodenparametern
* [Hibernate Validator: Referenzdokumentation](https://docs.jboss.org/hibernate/validator/6.0/reference/en-US/html_single/) — die verbreitete Referenzimplementierung
* [Open Liberty: Bean Validation 2.0 feature](https://openliberty.io/docs/latest/reference/feature/beanValidation-2.0.html) — Aktivierung in Liberty

## 🤔 Reflexionsfragen

* Wir validieren nun potenziell an **drei** Stellen: im HTML-Formular, im Servlet und im Service. Warum ist das sinnvoll – welche Aufgabe erfüllt jede Ebene?
* Welchen Vorteil hat Bean Validation gegenüber der Validierung mit `if`-Kaskaden?
* Was passiert, wenn im Service eine Validierung fehlschlägt, die das Servlet vorher **nicht** geprüft hatte? Wie äußert sich das für den Benutzer, und wie könnte man damit umgehen?
* Können wir `@Valid` auch im Servlet benutzen, statt den Validator programmatisch aufzurufen?
* Was liefert die Methode `validator.validate(...)` zurück?

