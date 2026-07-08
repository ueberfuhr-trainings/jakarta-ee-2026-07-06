---
layout: default
title: Dependency Injection mit CDI
---

# Dependency Injection mit CDI

Damit sich mehrere Servlets denselben Service teilen, musstest Du ihn bisher von
Hand als Singleton bauen. Das ist fehleranfällig und vermischt fachliche Logik
mit technischer Infrastruktur (Lebensdauer, gemeinsame Nutzung). **CDI**
(*Contexts and Dependency Injection*) nimmt Dir das ab: Der Container verwaltet
die Objekte und stellt sie dort bereit, wo sie gebraucht werden. Die
Code-Änderung ist klein – der Schwerpunkt dieser Übung liegt daher auf dem
**Verständnis der Konzepte**.

## 🎯 Lernziele

* Du verstehst, was Dependency Injection und Inversion of Control sind und welches Problem sie lösen.
* Du kennst die drei Arten der Injection: Constructor-, Field- und Method-Injection.
* Du verstehst, wie der Container eine Injection auflöst: by-type, by-name und by-qualifier.
* Du kannst eine Klasse als CDI-Bean mit einem Scope (`@ApplicationScoped`) bereitstellen und per `@Inject` injizieren.
* Du kannst CDI in Open Liberty aktivieren.

## ✅ Definition of Done

* [ ] Der Service ist kein von Hand gebautes Singleton mehr, sondern ein CDI-Bean mit `@ApplicationScoped`.
* [ ] Die Servlets erhalten den Service per `@Inject` – es gibt kein `new` mehr für den Service.
* [ ] Ein hinzugefügtes Todo erscheint weiterhin bei der Ausgabe (der Service wird also weiterhin geteilt).
* [ ] Das CDI-Feature ist in der Liberty-Konfiguration aktiv.
* [ ] Ihr habt die Reflexions- und Recherchefragen schriftlich beantwortet.

## 🪜 Arbeitsschritte

1. **CDI in Liberty aktivieren.** In der Liberty-Konfiguration muss das CDI-Feature aktiv sein (`cdi-2.0`).
   - Das umfassende Feature `jakartaee-8.0` enthält es bereits. Hast Du in einer früheren Übung auf einen minimalen Feature-Satz reduziert, ergänze `<feature>cdi-2.0</feature>`.
   - Historisch war zusätzlich eine leere Marker-Datei `src/main/webapp/WEB-INF/beans.xml` nötig, damit der Container die Anwendung nach Beans durchsucht. Recherchiere, ob (und in welcher Form) Du sie in dieser CDI-Version noch brauchst, und lege sie bei Bedarf an:
     ```xml
     <?xml version="1.0" encoding="UTF-8"?>
     <beans xmlns="http://xmlns.jcp.org/xml/ns/javaee"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                                http://xmlns.jcp.org/xml/ns/javaee/beans_2_0.xsd"
            version="2.0" bean-discovery-mode="annotated">
     </beans>
     ```
2. **Service zum CDI-Bean machen.** Entferne die manuelle Singleton-Implementierung aus dem Service und annotiere die Klasse stattdessen mit `@ApplicationScoped` (`javax.enterprise.context.ApplicationScoped`). Der Container sorgt nun dafür, dass es genau **eine** Instanz für die gesamte Anwendung gibt.
3. **Service injizieren.** Ersetze in allen Servlets, die den Service bisher per `new` erzeugt haben, die manuelle Erzeugung durch eine per `@Inject` (`javax.inject.Inject`) injizierte Referenz.
4. **Testen.** Baue und starte die Anwendung mit `mvn liberty:dev`. Lege ein Todo an und prüfe über die Ausgabe, dass es erscheint – der Service wird also weiterhin von allen Servlets gemeinsam genutzt, obwohl Du selbst kein Singleton mehr programmiert hast.

## 📚 Selbstlernmaterial

* [Jakarta Contexts and Dependency Injection: Specification](https://jakarta.ee/specifications/cdi/) — offizielle CDI-Spezifikation
* [Martin Fowler: Inversion of Control Containers and the Dependency Injection pattern](https://martinfowler.com/articles/injection.html) — das grundlegende Konzept, inkl. Constructor- vs. Setter-Injection
* [Baeldung: Java CDI](https://www.baeldung.com/java-ee-cdi) — Beans, `@Inject`, Scopes und Qualifier in der Praxis
* [Jakarta EE Tutorial: Introduction to Jakarta Contexts and Dependency Injection](https://eclipse-ee4j.github.io/jakartaee-tutorial/#introduction-to-jakarta-contexts-and-dependency-injection) — Konzepte von CDI (Beans, Injection, Scopes, Qualifier) erklärt
* [Open Liberty: CDI 2.0 feature](https://openliberty.io/docs/latest/reference/feature/cdi-2.0.html) — Aktivierung von CDI in Liberty

## 🤔 Reflexions- und Recherchefragen

* Was versteht man unter „Dependency Injection" und „Inversion of Control"? Welches konkrete Problem aus den vorigen Übungen (der von Hand gebaute Singleton) löst CDI damit?
* Was muss man im Code tun, damit eine Klasse als Bean bereitsteht und an anderer Stelle injiziert werden kann?
* Was ist der Unterschied zwischen **Constructor-**, **Field-** und **Method-(Setter-)Injection**? Welche Variante ist wann sinnvoll, und welche lässt sich z. B. besser testen?
* Wie entscheidet der Container, **welche** Instanz injiziert wird? Erkläre die Auflösung **by-type**, **by-name** und **by-qualifier** – und was passiert, wenn es für einen Typ mehrere passende Beans gibt.
* Wofür steht der Scope `@ApplicationScoped`? Welche weiteren Scopes gibt es und wann setzt man sie ein?
* In der Datei `beans.xml` gibt es eine Einstellung `bean-discovery-mode`. Was bedeutet diese und welche Werte sind möglich?
