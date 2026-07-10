package de.schulung.jakartaee.todos;

import java.time.LocalDate;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
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
        return em.createQuery("SELECT t FROM Todo t", Todo.class)
                .getResultList();
    }

    public Collection<Todo> getTodos(String search) {
        return em.createQuery(
                "SELECT t FROM Todo t WHERE LOWER(t.title) LIKE :search", Todo.class)
                .setParameter("search", "%" + search.toLowerCase() + "%")
                .getResultList();
    }

    @Transactional
    public void addTodo(@Valid Todo todo) {
        em.persist(todo);
    }

    /**
     * Legt beim Start der Anwendung einmalig zwei Beispiel-Todos an, sofern die
     * Datenbank noch leer ist. So bleiben die Daten über Neustarts erhalten,
     * ohne bei jedem Start dupliziert zu werden.
     */
    @Transactional
    void seedSampleData(@Observes @Initialized(ApplicationScoped.class) Object init) {
        long count = em.createQuery("SELECT COUNT(t) FROM Todo t", Long.class)
                .getSingleResult();
        if (count == 0) {
            em.persist(new Todo()
                    .setTitle("Einkaufen gehen")
                    .setDescription("Milch, Brot und Butter besorgen")
                    .setDueDate(LocalDate.now().plusWeeks(1)));
            em.persist(new Todo()
                    .setTitle("Übung vorbereiten")
                    .setDescription("Servlet-Übung für die Schulung fertigstellen")
                    .setDueDate(LocalDate.now().plusDays(3))
                    .setStatus(TodoStatus.IN_ARBEIT));
        }
    }

}
