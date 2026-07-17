package de.schulung.spring.todos.persistence;

import org.mapstruct.Mapper;

import de.schulung.spring.todos.domain.Todo;

/**
 * Wandelt zwischen dem Domänenmodell {@link Todo} und der JPA-{@link TodoEntity}
 * um. Die Implementierung erzeugt MapStruct zur Compile-Zeit;
 * {@code componentModel = "spring"} macht daraus eine injizierbare Spring-Bean.
 */
@Mapper(componentModel = "spring")
public interface TodoEntityMapper {

    Todo toDomain(TodoEntity entity);

    TodoEntity toEntity(Todo todo);

}
