package de.schulung.quarkus.todos.persistence;

import org.mapstruct.Mapper;

import de.schulung.quarkus.todos.domain.Todo;

/**
 * Wandelt zwischen dem Domänenmodell {@link Todo} und der JPA-{@link TodoEntity}
 * um. Die Implementierung erzeugt MapStruct zur Compile-Zeit;
 * {@code componentModel = "jakarta-cdi"} macht daraus eine injizierbare CDI-Bean.
 */
@Mapper(componentModel = "jakarta-cdi")
public interface TodoEntityMapper {

    Todo toDomain(TodoEntity entity);

    TodoEntity toEntity(Todo todo);

}
