package de.schulung.spring.todos.boundary.rest;

import org.mapstruct.Mapper;

import de.schulung.spring.todos.domain.Todo;
import de.schulung.spring.todos.domain.TodoStatus;

/**
 * Wandelt zwischen dem Domänenmodell {@link Todo} und dem REST-{@link TodoDto}
 * um. Die Implementierung erzeugt MapStruct zur Compile-Zeit;
 * {@code componentModel = "spring"} macht daraus eine injizierbare Spring-Bean.
 * Der Status wird über die {@code default}-Methoden zwischen dem internen Enum
 * und den API-Werten {@code ready} / {@code in_progress} / {@code done} übersetzt.
 */
@Mapper(componentModel = "spring")
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
        if (status == null) {
            // MapStruct ruft diese Methode auch bei fehlendem status auf -> Default
            return TodoStatus.ERSTELLT;
        }
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
