package de.schulung.jakartaee.todos.persistence;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import de.schulung.jakartaee.todos.domain.Todo;
import de.schulung.jakartaee.todos.domain.TodosDao;

/**
 * JPA-basierte Implementierung von {@link TodosDao}. Nach außen (zur Domäne)
 * spricht sie das Domänenmodell {@link Todo}; intern arbeitet sie mit der
 * {@link TodoEntity} und wandelt über den {@link TodoEntityMapper} um.
 *
 * <p>{@code count()} und {@code findByTitleContains(String)} werden gegenüber den
 * {@code default}-Methoden aus {@link TodosDao} überschrieben, um die Arbeit
 * effizient in die Datenbank zu verlagern (JPQL-{@code COUNT} bzw. {@code LIKE}).</p>
 */
@ApplicationScoped
public class JpaTodosDao implements TodosDao {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private TodoEntityMapper mapper;

    @Override
    public Collection<Todo> findAll() {
        return em
                .createQuery("SELECT t FROM TodoEntity t", TodoEntity.class)
                .getResultList()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void save(Todo todo) {
        em.persist(mapper.toEntity(todo));
    }

    @Override
    @Transactional
    public boolean deleteById(long id) {
        TodoEntity entity = em.find(TodoEntity.class, id);
        if (entity == null) {
            return false;
        }
        em.remove(entity);
        return true;
    }

    @Override
    public long count() {
        return em
                .createQuery("SELECT COUNT(t) FROM TodoEntity t", Long.class)
                .getSingleResult();
    }

    @Override
    public Collection<Todo> findByTitleContains(String search) {
        return em
                .createQuery(
                        "SELECT t FROM TodoEntity t WHERE LOWER(t.title) LIKE :search",
                        TodoEntity.class
                )
                .setParameter("search", "%" + search.toLowerCase() + "%")
                .getResultList()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

}
