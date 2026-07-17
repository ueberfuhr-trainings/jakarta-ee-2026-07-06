package de.schulung.quarkus.todos.persistence;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import de.schulung.quarkus.todos.domain.Todo;
import de.schulung.quarkus.todos.domain.TodosDao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * Panache-/JPA-basierte Implementierung von {@link TodosDao}. Nach außen (zur
 * Domäne) spricht sie das Domänenmodell {@link Todo}; intern arbeitet sie mit der
 * {@link TodoEntity} und wandelt über den {@link TodoEntityMapper} um. Der
 * Datenzugriff läuft über das Panache-{@link TodoRepository} – er steckt also in
 * der Persistenzschicht, nicht in der Domäne. Den Konstruktor für die
 * {@code final}-Felder erzeugt Lombok ({@code @RequiredArgsConstructor}).
 */
@ApplicationScoped
@Transactional
@RequiredArgsConstructor
public class JpaTodosDao implements TodosDao {

    private final TodoRepository repository;
    private final TodoEntityMapper mapper;

    @Override
    public Collection<Todo> findAll() {
        return repository.listAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Todo todo) {
        TodoEntity entity = mapper.toEntity(todo);
        repository.persist(entity);
        // generierte id in das übergebene Todo zurückschreiben
        todo.setId(entity.getId());
    }

    @Override
    public boolean deleteById(long id) {
        return repository.deleteById(id);
    }

    @Override
    public Optional<Todo> findById(long id) {
        return Optional.ofNullable(repository.findById(id)).map(mapper::toDomain);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public Collection<Todo> findByTitleContains(String search) {
        return repository.findByTitleContainingIgnoreCase(search)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

}
