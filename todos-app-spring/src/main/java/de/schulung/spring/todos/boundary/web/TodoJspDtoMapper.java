package de.schulung.spring.todos.boundary.web;

import org.springframework.stereotype.Component;

import de.schulung.spring.todos.domain.Todo;
import de.schulung.spring.todos.domain.TodoStatus;

/**
 * Wandelt das Domänenmodell {@link Todo} in das Anzeige-DTO {@link TodoJspDto}
 * um (nur diese Richtung wird für die Anzeige gebraucht).
 */
@Component
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
