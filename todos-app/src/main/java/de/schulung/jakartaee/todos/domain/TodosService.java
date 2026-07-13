package de.schulung.jakartaee.todos.domain;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;

/**
 * Fachliche Verwaltung der Todos. Der {@link TodosService} enthält die
 * Business-Logik (u.a. die Validierung neuer Todos) und delegiert den reinen
 * Datenzugriff an das {@link TodosDao}. Er kennt dabei nur das Interface, nicht
 * dessen konkrete (JPA-)Implementierung.
 */
@ApplicationScoped
public class TodosService {

    @Inject
    private TodosDao todosDao;

    public Collection<Todo> getTodos() {
        return todosDao.findAll();
    }

    public Collection<Todo> getTodos(String search) {
        return todosDao.findByTitleContains(search);
    }

    public void addTodo(@Valid Todo todo) {
        todosDao.save(todo);
    }

    /**
     * Liefert die Anzahl der gespeicherten Todos. Nützlich, um beim Start der
     * Anwendung zu entscheiden, ob Beispieldaten angelegt werden müssen.
     */
    public long count() {
        return todosDao.count();
    }

}
