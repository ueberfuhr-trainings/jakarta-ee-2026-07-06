package de.schulung.spring.todos.domain;

import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * Fachliche Verwaltung der Todos. Der Service enthält die Business-Logik und
 * delegiert den reinen Datenzugriff an das {@link TodosDao}. Er kennt dabei nur
 * das Interface, nicht dessen konkrete (Spring-Data-/JPA-)Implementierung.
 */
@Service
public class TodosService {

    private final TodosDao todosDao;

    public TodosService(TodosDao todosDao) {
        this.todosDao = todosDao;
    }

    public Collection<Todo> getTodos() {
        return todosDao.findAll();
    }

    public Collection<Todo> getTodos(String search) {
        return todosDao.findByTitleContains(search);
    }

    public Optional<Todo> getTodo(long id) {
        return todosDao.findById(id);
    }

    public void addTodo(Todo todo) {
        todosDao.save(todo);
    }

    public boolean deleteTodo(long id) {
        return todosDao.deleteById(id);
    }

    public long count() {
        return todosDao.count();
    }

}
