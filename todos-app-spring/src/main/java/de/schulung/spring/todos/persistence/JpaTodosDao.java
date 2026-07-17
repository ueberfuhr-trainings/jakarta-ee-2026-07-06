package de.schulung.spring.todos.persistence;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import de.schulung.spring.todos.domain.Todo;
import de.schulung.spring.todos.domain.TodosDao;

/**
 * Spring-Data-/JPA-basierte Implementierung von {@link TodosDao}. Nach außen
 * (zur Domäne) spricht sie das Domänenmodell {@link Todo}; intern arbeitet sie
 * mit der {@link TodoEntity} und wandelt über den {@link TodoEntityMapper} um.
 *
 * <p>Statt eines {@code EntityManager} wird hier das Spring-Data-Repository
 * {@link TodoRepository} injiziert und genutzt – der Datenzugriff steckt also
 * in der Persistenzschicht, nicht in der Domäne.</p>
 */
@Repository
public class JpaTodosDao implements TodosDao {

    private final TodoRepository repository;
    private final TodoEntityMapper mapper;

    public JpaTodosDao(TodoRepository repository, TodoEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Collection<Todo> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void save(Todo todo) {
        TodoEntity entity = mapper.toEntity(todo);
        // saveAndFlush erzwingt das INSERT sofort, damit die generierte id
        // verfügbar ist (sonst setzt EclipseLink unter JTA die id erst beim Commit).
        repository.saveAndFlush(entity);
        // generierte id in das übergebene Todo zurückschreiben
        todo.setId(entity.getId());
    }

    @Override
    @Transactional
    public boolean deleteById(long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    @Override
    public Optional<Todo> findById(long id) {
        return repository.findById(id).map(mapper::toDomain);
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
