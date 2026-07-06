---
name: Übungsaufgabe erstellen
description: Erstellt eine neue Übungsaufgabe im Ordner docs/ nach dem einheitlichen Muster der Schulungs-Doku (Jekyll-Site). Verwenden, wenn eine neue Schulungs-Übung/Aufgabe/Übungsaufgabe für die Doku angelegt oder bereitgestellt werden soll.
---

# Übungsaufgabe erstellen

Neue Übungsaufgaben werden im Ordner `docs/` bereitgestellt. Die Doku ist eine
Jekyll-Site (Theme `minima`, deutschsprachig, `kramdown`). Alle Übungen folgen
demselben Aufbau, Tonfall und derselben Formatierung – dieser Skill beschreibt
diese vollständig.

## Ablauf

1. **Kategorie & Nummer bestimmen.** Übungen liegen unter
   `docs/<NN>-<kategorie>/<MM>-<thema>/issue.md`, z. B.
   `docs/01-basics/01-http/issue.md`. `<NN>` ist die zweistellige Nummer der
   Kategorie (z. B. `01-basics`), `<MM>` die zweistellige, fortlaufende Nummer
   der Übung innerhalb der Kategorie. Wähle die nächste freie `<MM>` bzw. lege
   bei Bedarf eine neue Kategorie an.
2. **Ordner + `issue.md` anlegen** unter dem gewählten Pfad.
3. **`issue.md` nach der Vorlage füllen** (siehe unten).
4. **In `docs/index.md` eintragen** – in der passenden Kategorie-Tabelle eine
   Zeile mit `Nr.` und Link (`[<Titel>](./<NN>-<kategorie>/<MM>-<thema>/issue.md)`)
   ergänzen. Falls die Kategorie neu ist, zusätzlich eine `##`-Überschrift plus
   Tabellenkopf (`| Nr. | Übung |`) anlegen.

## Tonfall & Stil

* Sprache: **Deutsch**. Anrede in der **Du-Form** (bei Schritten, die in der
  Gruppe erfolgen, „Ihr").
* Motivierend und praxisnah: Die Einleitung stellt einen Bezug zur echten
  Anwendung her, bevor es an die Aufgabe geht.
* Lernziele werden aus Lernenden-Sicht formuliert und beginnen mit
  „Du verstehst … / Du kannst … / Du kennst …".
* Knapp und konkret; jede Aufgabe soll ohne weitere Rückfragen bearbeitbar sein.

## Struktur von `issue.md`

Feste Abschnitte in genau dieser Reihenfolge, jeweils mit der angegebenen
Emoji-Überschrift:

1. **Jekyll-Frontmatter** mit `layout: default` und `title`.
2. **`#`-Titel** (identisch zum `title`) + kurzer Einleitungsabsatz, der Thema
   und Ziel der Übung motiviert.
3. `## 🎯 Lernziele` – Aufzählung (`*`) der Lernziele.
4. `## ✅ Definition of Done` – Checkboxen-Liste (`* [ ] …`) mit den konkret
   überprüfbaren Ergebnissen.
5. `## 🪜 Arbeitsschritte` – nummerierte Liste (`1.`, `2.`, …) der
   Handlungsschritte. Details/Unterpunkte als eingerückte `-`-Liste.
6. `## 📚 Selbstlernmaterial` – Aufzählung (`*`) weiterführender Links im Format
   `[Quelle: Titel](URL) — kurze Beschreibung`.
7. `## 🤔 Reflexionsfragen` – Aufzählung (`*`) offener Fragen zur Reflexion bzw.
   gemeinsamen Diskussion.

## Vollständige Vorlage

````markdown
---
layout: default
title: <Titel der Übung>
---

# <Titel der Übung>

<Einleitungsabsatz: Was ist das Thema, warum ist es relevant, was wird in dieser
Übung praktisch getan? 2–4 Sätze, Du-Form.>

## 🎯 Lernziele

* Du verstehst, <…>.
* Du kannst <…>.
* Du kennst <…>.

## ✅ Definition of Done

* [ ] <überprüfbares Ergebnis 1>
* [ ] <überprüfbares Ergebnis 2>
* [ ] Ihr habt die Reflexionsfragen schriftlich beantwortet.

## 🪜 Arbeitsschritte

1. <erster Schritt>
2. <zweiter Schritt>
3. <Schritt mit Details>
   - <Detail / zu untersuchender Aspekt>
   - <Detail / zu untersuchender Aspekt>
4. Beantwortet gemeinsam die Reflexionsfragen.

## 📚 Selbstlernmaterial

* [Quelle: Titel](https://example.com) — kurze Beschreibung
* [Quelle: Titel](https://example.com) — kurze Beschreibung

## 🤔 Reflexionsfragen

* <offene Frage zum Nachdenken>
* <offene Frage zum Nachdenken>
````
