package de.schulung.jakartaee.todos.persistence;

import org.mapstruct.Mapper;

import de.schulung.jakartaee.todos.domain.Todo;

/**
 * Wandelt zwischen dem Domänenmodell {@link Todo} und der JPA-{@link TodoEntity}
 * um. Die Implementierung erzeugt MapStruct zur Compile-Zeit;
 * {@code componentModel = "cdi"} macht daraus eine injizierbare CDI-Bean.
 */
@Mapper(componentModel = "cdi")
public interface TodoEntityMapper {

    Todo toDomain(TodoEntity entity);

    TodoEntity toEntity(Todo todo);

}
