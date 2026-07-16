package de.schulung.jakartaee.todos.boundary.rest;

import javax.enterprise.context.ApplicationScoped;

import org.mapstruct.Mapper;

import de.schulung.jakartaee.todos.domain.Todo;
import de.schulung.jakartaee.todos.domain.TodoStatus;

/**
 * Wandelt zwischen dem Domänenmodell {@link Todo} und dem REST-{@link TodoDto}
 * um. Der Status wird dabei zwischen dem internen Enum und den API-Werten
 * {@code ready} / {@code in_progress} / {@code done} übersetzt.
 */
@Mapper(componentModel = "cdi")
public interface TodoDtoMapper {

    TodoDto toDto(Todo todo);

    Todo toDomain(TodoDto dto);

    default String toApiStatus(TodoStatus status) {
        if (status == null) {
            return null;
        }
        switch (status) {
            case ERSTELLT:
                return "ready";
            case IN_ARBEIT:
                return "in_progress";
            case FERTIG:
                return "done";
            default:
                throw new IllegalArgumentException("Unbekannter Status: " + status);
        }
    }

    default TodoStatus fromApiStatus(String status) {
        switch (status) {
            case "ready":
                return TodoStatus.ERSTELLT;
            case "in_progress":
                return TodoStatus.IN_ARBEIT;
            case "done":
                return TodoStatus.FERTIG;
            default:
                // unbekannte/leere Werte -> Default
                return TodoStatus.ERSTELLT;
        }
    }

}
