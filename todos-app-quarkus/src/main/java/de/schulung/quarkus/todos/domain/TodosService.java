package de.schulung.quarkus.todos.domain;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import de.schulung.quarkus.todos.persistence.TodoEntity;
import de.schulung.quarkus.todos.persistence.TodoEntityMapper;
import de.schulung.quarkus.todos.persistence.TodoRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

/**
 * Fachliche Verwaltung der Todos. Nutzt das Panache-{@link TodoRepository} für
 * den Datenzugriff und wandelt zwischen {@link TodoEntity} und dem
 * Domänenmodell {@link Todo} um.
 */
@ApplicationScoped
@Transactional
public class TodosService {

    private final TodoRepository repository;
    private final TodoEntityMapper mapper;

    public TodosService(TodoRepository repository, TodoEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Collection<Todo> getTodos() {
        return repository.listAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    public Collection<Todo> getTodos(String search) {
        return repository.findByTitleContainingIgnoreCase(search)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    public Optional<Todo> getTodo(long id) {
        return Optional.ofNullable(repository.findById(id)).map(mapper::toDomain);
    }

    public void addTodo(Todo todo) {
        TodoEntity entity = mapper.toEntity(todo);
        repository.persist(entity);
        // generierte id in das übergebene Todo zurückschreiben
        todo.setId(entity.getId());
    }

    public boolean deleteTodo(long id) {
        return repository.deleteById(id);
    }

    public long count() {
        return repository.count();
    }

}
