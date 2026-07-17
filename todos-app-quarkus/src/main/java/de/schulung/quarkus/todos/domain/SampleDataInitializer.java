package de.schulung.quarkus.todos.domain;

import java.time.LocalDate;
import java.util.logging.Logger;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.runtime.StartupEvent;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Legt beim Start der Anwendung einmalig die Beispiel-Todos an, sofern die
 * Datenbank noch leer ist. Beobachtet das Quarkus-{@link StartupEvent}. Ob
 * überhaupt Beispieldaten angelegt werden, steuert die per MicroProfile Config
 * injizierte Property {@code todos.sampledata.enabled} (wie in der
 * Jakarta-EE-App). Konvention ist {@code false} (deaktiviert).
 */
@ApplicationScoped
public class SampleDataInitializer {

    private static final Logger LOGGER = Logger.getLogger(SampleDataInitializer.class.getName());

    @Inject
    TodosService todosService;

    @Inject
    @ConfigProperty(name = "todos.sampledata.enabled", defaultValue = "false")
    boolean sampleDataEnabled;

    @Transactional
    void onStartup(@Observes StartupEvent event) {
        if (!sampleDataEnabled) {
            LOGGER.info("Sample data initialization is disabled "
                    + "(set todos.sampledata.enabled=true to enable it).");
            return;
        }
        LOGGER.info("Sample Data Initializer checking current database entries...");
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
