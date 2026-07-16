package de.schulung.quarkus.todos.boundary.rest;

import de.schulung.quarkus.todos.domain.Todo;
import de.schulung.quarkus.todos.domain.TodoStatus;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Wandelt zwischen dem Domänenmodell {@link Todo} und dem REST-{@link TodoDto}
 * um. Der Status wird dabei zwischen dem internen Enum und den API-Werten
 * {@code ready} / {@code in_progress} / {@code done} übersetzt.
 */
@ApplicationScoped
public class TodoDtoMapper {

    public TodoDto toDto(Todo todo) {
        if (todo == null) {
            return null;
        }
        TodoDto dto = new TodoDto();
        dto.setId(todo.getId());
        dto.setTitle(todo.getTitle());
        dto.setDescription(todo.getDescription());
        dto.setDueDate(todo.getDueDate());
        dto.setStatus(toApiStatus(todo.getStatus()));
        return dto;
    }

    public Todo toDomain(TodoDto dto) {
        if (dto == null) {
            return null;
        }
        // Die id wird beim Anlegen nicht übernommen (der Server vergibt sie).
        Todo todo = new Todo()
                .setTitle(dto.getTitle())
                .setDescription(dto.getDescription())
                .setDueDate(dto.getDueDate());
        if (dto.getStatus() != null) {
            todo.setStatus(fromApiStatus(dto.getStatus()));
        }
        return todo;
    }

    private String toApiStatus(TodoStatus status) {
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

    private TodoStatus fromApiStatus(String status) {
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
