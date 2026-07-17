package de.schulung.quarkus.todos.domain;

import java.util.Collection;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;

/**
 * Fachliche Verwaltung der Todos. Der Service enthält die Business-Logik und
 * delegiert den reinen Datenzugriff an das {@link TodosDao}. Er kennt dabei nur
 * das Interface, nicht dessen konkrete (Panache-/JPA-)Implementierung. Den
 * Konstruktor für die {@code final}-Felder erzeugt Lombok
 * ({@code @RequiredArgsConstructor}); ArC nutzt ihn für Constructor-Injection.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class TodosService {

    private final TodosDao todosDao;

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
