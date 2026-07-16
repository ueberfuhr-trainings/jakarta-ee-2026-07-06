package de.schulung.jakartaee.todos.domain;

import java.time.LocalDate;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Legt beim Start der Anwendung einmalig die Beispiel-Todos an, sofern die
 * Datenbank noch leer ist. Beobachtet das CDI-Event für den initialisierten
 * {@code ApplicationScoped}-Kontext und nutzt den {@link TodosService}, um zu
 * prüfen, ob bereits Daten vorhanden sind, und um die Todos zu speichern.
 * <p>
 * Ob überhaupt Beispieldaten angelegt werden, steuert die per MicroProfile
 * Config injizierte Property {@code todos.sampledata.enabled}. Konvention ist
 * {@code false} (deaktiviert); der Initializer wird also nur aktiv, wenn die
 * Property über eine Config-Quelle (z.B. {@code META-INF/microprofile-config.properties},
 * eine Umgebungsvariable oder System-Property) explizit auf {@code true} gesetzt wird.
 */
@ApplicationScoped
public class SampleDataInitializer {

    private static final Logger LOGGER = Logger.getLogger(SampleDataInitializer.class.getName());

    @Inject
    private TodosService todosService;

    @Inject
    @ConfigProperty(name = "todos.sampledata.enabled", defaultValue = "false")
    private boolean sampleDataEnabled;

    void onStartup(@Observes @Initialized(ApplicationScoped.class) Object init) {
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
