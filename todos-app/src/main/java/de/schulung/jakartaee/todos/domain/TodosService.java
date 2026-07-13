package de.schulung.jakartaee.todos.domain;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

/**
 * Verwaltet die Todos in der Datenbank. Der {@link EntityManager} wird per
 * {@code @PersistenceContext} bereitgestellt; schreibende Operationen laufen in
 * einer Transaktion.
 */
@ApplicationScoped
public class TodosService {

    @PersistenceContext
    private EntityManager em;

    public Collection<Todo> getTodos() {
        return em
        		.createQuery("SELECT t FROM Todo t", Todo.class)
                .getResultList();
    }

    public Collection<Todo> getTodos(String search) {
        return em
        		.createQuery(
        				"SELECT t FROM Todo t WHERE LOWER(t.title) LIKE :search", 
        				Todo.class
        			)
                .setParameter("search", "%" + search.toLowerCase() + "%")
                .getResultList();
    }

    @Transactional
    public void addTodo(@Valid Todo todo) {
        em.persist(todo);
    }

    /**
     * Liefert die Anzahl der gespeicherten Todos. Nützlich, um beim Start der
     * Anwendung zu entscheiden, ob Beispieldaten angelegt werden müssen.
     */
    public long count() {
        return em
        		.createQuery("SELECT COUNT(t) FROM Todo t", Long.class)
                .getSingleResult();
    }

}
