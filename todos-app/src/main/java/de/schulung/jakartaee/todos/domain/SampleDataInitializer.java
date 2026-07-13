package de.schulung.jakartaee.todos.domain;

import java.time.LocalDate;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * Legt beim Start der Anwendung einmalig die Beispiel-Todos an, sofern die
 * Datenbank noch leer ist. Beobachtet das CDI-Event für den initialisierten
 * {@code ApplicationScoped}-Kontext und nutzt den {@link TodosService}, um zu
 * prüfen, ob bereits Daten vorhanden sind, und um die Todos zu speichern.
 */
@ApplicationScoped
public class SampleDataInitializer {

    @Inject
    private TodosService todosService;

    void onStartup(@Observes @Initialized(ApplicationScoped.class) Object init) {
        if (todosService.count() > 0) {
            return;
        }
        todosService.addTodo(new Todo()
                .setTitle("Einkaufen gehen")
                .setDescription("Milch, Brot und Butter besorgen")
                .setDueDate(LocalDate.now().plusWeeks(1)));
        todosService.addTodo(new Todo()
                .setTitle("Übung vorbereiten")
                .setDescription("Servlet-Übung für die Schulung fertigstellen")
                .setDueDate(LocalDate.now().plusDays(3))
                .setStatus(TodoStatus.IN_ARBEIT));
    }

}
