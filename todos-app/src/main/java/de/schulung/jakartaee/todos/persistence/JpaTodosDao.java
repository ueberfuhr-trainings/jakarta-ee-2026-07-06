package de.schulung.jakartaee.todos.persistence;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import de.schulung.jakartaee.todos.domain.Todo;
import de.schulung.jakartaee.todos.domain.TodosDao;

/**
 * JPA-basierte Implementierung von {@link TodosDao}. Der {@link EntityManager}
 * wird per {@code @PersistenceContext} bereitgestellt. Diese Klasse kennt die
 * Domäne (implementiert deren Interface) – die Domäne kennt umgekehrt nur das
 * Interface, nicht diese Implementierung.
 *
 * <p>{@code count()} und {@code findByTitleContains(String)} werden gegenüber den
 * {@code default}-Methoden aus {@link TodosDao} überschrieben, um die Arbeit
 * effizient in die Datenbank zu verlagern (JPQL-{@code COUNT} bzw. {@code LIKE}),
 * statt alle Todos in den Speicher zu laden.</p>
 */
@ApplicationScoped
public class JpaTodosDao implements TodosDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Collection<Todo> findAll() {
        return em
                .createQuery("SELECT t FROM Todo t", Todo.class)
                .getResultList();
    }

    @Override
    @Transactional
    public void save(Todo todo) {
        em.persist(todo);
    }

    @Override
    public long count() {
        return em
                .createQuery("SELECT COUNT(t) FROM Todo t", Long.class)
                .getSingleResult();
    }

    @Override
    public Collection<Todo> findByTitleContains(String search) {
        return em
                .createQuery(
                        "SELECT t FROM Todo t WHERE LOWER(t.title) LIKE :search",
                        Todo.class
                )
                .setParameter("search", "%" + search.toLowerCase() + "%")
                .getResultList();
    }

}
