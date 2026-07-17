package de.schulung.spring.todos.domain;

import java.time.LocalDate;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * Legt beim Start der Anwendung einmalig die Beispiel-Todos an, sofern die
 * Datenbank noch leer ist. Ob überhaupt Beispieldaten angelegt werden, steuert
 * die per Spring injizierte Property {@code todos.sampledata.enabled} (Pendant
 * zur MicroProfile-{@code @ConfigProperty} in der Jakarta-EE-App). Konvention ist
 * {@code false} (deaktiviert).
 * <p>Den Konstruktor für die {@code final}-Felder erzeugt Lombok
 * ({@code @RequiredArgsConstructor}); {@code sampleDataEnabled} wird als
 * {@code @Value}-Feld separat gesetzt.</p>
 */
@Component
@RequiredArgsConstructor
public class SampleDataInitializer {

    private static final Logger LOGGER = Logger.getLogger(SampleDataInitializer.class.getName());

    private final TodosService todosService;

    @Value("${todos.sampledata.enabled:false}")
    private boolean sampleDataEnabled;

    @EventListener(ContextRefreshedEvent.class)
    void onStartup() {
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
