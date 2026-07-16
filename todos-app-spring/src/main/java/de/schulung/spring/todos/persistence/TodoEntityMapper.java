package de.schulung.spring.todos.persistence;

import org.springframework.stereotype.Component;

import de.schulung.spring.todos.domain.Todo;

/**
 * Wandelt zwischen dem Domänenmodell {@link Todo} und der JPA-{@link TodoEntity}
 * um. Von Hand geschrieben (kein MapStruct o.Ä.), damit die Umwandlung sichtbar
 * bleibt. Als Spring-Bean ({@code @Component}) injizierbar.
 */
@Component
public class TodoEntityMapper {

    public Todo toDomain(TodoEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Todo()
                .setId(entity.getId())
                .setTitle(entity.getTitle())
                .setDescription(entity.getDescription())
                .setDueDate(entity.getDueDate())
                .setStatus(entity.getStatus());
    }

    public TodoEntity toEntity(Todo todo) {
        if (todo == null) {
            return null;
        }
        TodoEntity entity = new TodoEntity();
        entity.setId(todo.getId());
        entity.setTitle(todo.getTitle());
        entity.setDescription(todo.getDescription());
        entity.setDueDate(todo.getDueDate());
        entity.setStatus(todo.getStatus());
        return entity;
    }

}
