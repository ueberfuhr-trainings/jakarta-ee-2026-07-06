package de.schulung.spring.todos.domain;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.schulung.spring.todos.persistence.TodoEntity;
import de.schulung.spring.todos.persistence.TodoEntityMapper;
import de.schulung.spring.todos.persistence.TodoRepository;

/**
 * Fachliche Verwaltung der Todos. Nutzt das Spring-Data-Repository für den
 * Datenzugriff und wandelt zwischen {@link TodoEntity} und dem Domänenmodell
 * {@link Todo} um.
 */
@Service
@Transactional
public class TodosService {

    private final TodoRepository repository;
    private final TodoEntityMapper mapper;

    public TodosService(TodoRepository repository, TodoEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public Collection<Todo> getTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<Todo> getTodos(String search) {
        return repository.findByTitleContainingIgnoreCase(search)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<Todo> getTodo(long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    public void addTodo(Todo todo) {
        TodoEntity entity = mapper.toEntity(todo);
        repository.save(entity);
        // generierte id in das übergebene Todo zurückschreiben
        todo.setId(entity.getId());
    }

    public boolean deleteTodo(long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

}
