---
layout: default
title: Bean Validation – Custom Constraints
---

# Bean Validation – Custom Constraints

Die eingebauten Constraints (`@Size`, `@Pattern`, `@FutureOrPresent`, …) decken
viele Fälle ab, aber nicht alle – und dieselbe Kombination immer wieder an jedes
Feld zu schreiben, wird schnell unübersichtlich. In dieser Übung baust Du eigene
Constraints: Im ersten Teil fasst Du vorhandene Constraints zu einem
wiederverwendbaren **Composite Constraint** zusammen. Im zweiten Teil schreibst
Du einen echten neuen Constraint mit eigener **Validator-Implementierung**.

## 🎯 Lernziele

* Du verstehst den Aufbau einer Constraint-Annotation.
* Du kannst mehrere vorhandene Constraints zu einem wiederverwendbaren Composite Constraint zusammenfassen.
* Du kannst einen eigenen Constraint mit einer `ConstraintValidator`-Implementierung schreiben.
* Du verstehst, wann ein Constraint eine Validator-Implementierung braucht und wann nicht.

## ✅ Definition of Done

* [ ] Es gibt eine wiederverwendbare Annotation `@Title`, die die Regeln „3–100 Zeichen" und „beginnt mit einem Großbuchstaben" kombiniert; sie wird am Titel des `Todo` verwendet.
* [ ] Es gibt eine Annotation `@MaximumFuture` mit eigener `ConstraintValidator`-Implementierung, die prüft, dass ein Datum höchstens **3 Monate** in der Zukunft liegt; sie wird an der Frist des `Todo` verwendet.
* [ ] Ungültige Eingaben werden weiterhin abgelehnt (Servlet/Service wie in der vorigen Übung).
* [ ] Ihr habt die Reflexionsfragen schriftlich beantwortet.

## 🪜 Arbeitsschritte

### Teil 1: Composite Constraint `@Title`

Ein Composite Constraint ist selbst nur eine Annotation, die mit **anderen**
Constraints annotiert ist. Er braucht **keine** eigene Validator-Implementierung
– die Prüfung übernehmen die zusammengesetzten Constraints.

1. Lege eine Annotation `@Title` an und annotiere sie mit den Bausteinen einer Constraint-Annotation sowie den Constraints, die sie bündeln soll:
   ```java
   @Size(min = 3, max = 100)
   @Pattern(regexp = "^\\p{Lu}.*")          // beginnt mit einem Großbuchstaben
   @Constraint(validatedBy = {})            // kein eigener Validator
   @ReportAsSingleViolation                 // ein Verstoß statt mehrerer Teil-Verstöße
   @Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
             ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
   @Retention(RetentionPolicy.RUNTIME)
   public @interface Title {
       String message() default "Der Titel ist ungültig.";
       Class<?>[] groups() default {};
       Class<? extends Payload>[] payload() default {};
   }
   ```
   Alle Typen stammen aus `javax.validation` bzw. `javax.validation.constraints`. Die `@Target`-Werte entsprechen denen der eingebauten Constraints (z. B. `@NotNull`). `@ReportAsSingleViolation` fasst die zusammengesetzten Verstöße zu einer einzigen Meldung (der von `@Title`) zusammen.
2. Verwende am Titel des `Todo` nur noch `@Title` statt der einzelnen `@Size`/`@Pattern`-Annotationen.
3. **Teste**: Ein Titel wie `abc` (klein) oder `Ab` (zu kurz) muss abgelehnt werden, `Einkaufen` muss durchgehen.

### Teil 2: Eigener Constraint `@MaximumFuture` mit Validator

Für „das Datum liegt höchstens 3 Monate in der Zukunft" gibt es keinen
eingebauten Constraint – hier brauchst Du eine eigene Validator-Implementierung.

1. Lege die Annotation `@MaximumFuture` an. Sie verweist über `@Constraint` auf ihre Validator-Klasse:
   ```java
   @Constraint(validatedBy = MaximumFutureValidator.class)
   @Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
             ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
   @Retention(RetentionPolicy.RUNTIME)
   public @interface MaximumFuture {
       String message() default "Die Frist darf höchstens 3 Monate in der Zukunft liegen.";
       Class<?>[] groups() default {};
       Class<? extends Payload>[] payload() default {};
   }
   ```
2. Implementiere die Validator-Klasse `MaximumFutureValidator implements ConstraintValidator<MaximumFuture, LocalDate>`:
   - In `isValid(LocalDate value, ConstraintValidatorContext context)` prüfst Du, ob der Wert höchstens 3 Monate in der Zukunft liegt.
   - Behandle `null` als **gültig** (die Pflicht-Prüfung ist Aufgabe von `@NotNull`).
3. Verwende `@MaximumFuture` an der Frist des `Todo` (zusätzlich zu `@FutureOrPresent`).
4. **Teste**: Ein Datum in 2 Monaten muss durchgehen, ein Datum in 6 Monaten muss abgelehnt werden.
5. **(Optional) Konfigurierbar machen.** Bisher sind „3 Monate" fest verdrahtet. Mach die erlaubte Obergrenze pro Verwendung einstellbar, sodass sich derselbe Constraint z. B. auch für „6 Jahre" nutzen lässt. Informiere Dich selbst darüber, wie eine Annotation Parameter (Elemente) erhält und wie der Validator darauf zugreift.

## 📚 Selbstlernmaterial

* [Jakarta Bean Validation: Specification](https://jakarta.ee/specifications/bean-validation/) — u. a. Kapitel zu Composing Constraints
* [Hibernate Validator: Creating custom constraints](https://docs.jboss.org/hibernate/validator/6.0/reference/en-US/html_single/#validator-customconstraints) — eigene Constraints und `ConstraintValidator`-Implementierungen
* [Hibernate Validator: Constraint composition](https://docs.jboss.org/hibernate/validator/6.0/reference/en-US/html_single/#section-constraint-composition) — mehrere Constraints zu einem zusammenfassen
* [Baeldung: Java Bean Validation Basics](https://www.baeldung.com/javax-validation) — Grundlagen zu Constraints und Validierung

## 🤔 Reflexionsfragen

* Warum braucht der Composite Constraint `@Title` **keine** Validator-Implementierung, `@MaximumFuture` aber schon?
* Welchen Vorteil bringt es, `@Title` als eigene Annotation zu definieren, statt `@Size` und `@Pattern` überall einzeln zu wiederholen?
* Wozu dient das Element `message()`, welches jede Constraint-Annotation neben`groups()` und `payload()` haben muss?
* Warum sollte ein `ConstraintValidator` den Wert `null` als gültig behandeln? Was wäre die Folge, wenn er es nicht täte?
* Warum ist der generische Typ des `ConstraintValidator` hier `LocalDate`? Was müsstest Du tun, damit derselbe Constraint auch für andere Datumstypen (z.B. `java.util.Date`) funktioniert?
