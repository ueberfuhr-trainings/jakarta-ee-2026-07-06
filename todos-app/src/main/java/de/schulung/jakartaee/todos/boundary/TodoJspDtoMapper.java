package de.schulung.jakartaee.todos.boundary;

import javax.enterprise.context.ApplicationScoped;

import de.schulung.jakartaee.todos.domain.Todo;
import de.schulung.jakartaee.todos.domain.TodoStatus;

/**
 * Wandelt das Domänenmodell {@link Todo} in das Anzeige-DTO {@link TodoJspDto}
 * um. Nur diese eine Richtung wird gebraucht (die Anzeige), daher gibt es kein
 * {@code toDomain}. Von Hand geschrieben (kein MapStruct o.Ä.).
 */
@ApplicationScoped
public class TodoJspDtoMapper {

    public TodoJspDto toJspDto(Todo todo) {
        if (todo == null) {
            return null;
        }
        TodoJspDto dto = new TodoJspDto();
        dto.setId(todo.getId());
        dto.setTitle(todo.getTitle());
        dto.setDescription(todo.getDescription());
        dto.setDueDate(todo.getDueDate());
        dto.setStatus(mapStatus(todo.getStatus()));
        return dto;
    }

    /**
     * Bildet den Status auf eine Anzeige-Bezeichnung ab. Bewusst per
     * {@code switch}, nicht über {@link Enum#name()}: So bleibt die Anzeige
     * unabhängig von den internen Enum-Namen und ist an einer Stelle steuerbar.
     */
    private String mapStatus(TodoStatus status) {
        if (status == null) {
            return null;
        }
        switch (status) {
            case ERSTELLT:
                return "Erstellt";
            case IN_ARBEIT:
                return "In Arbeit";
            case FERTIG:
                return "Fertig";
            default:
                throw new IllegalArgumentException("Unbekannter Status: " + status);
        }
    }

}
