package de.schulung.quarkus.todos.persistence;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Panache-Repository für {@link TodoEntity}. Durch das Implementieren von
 * {@link PanacheRepository} stehen die gängigen Datenzugriffs-Operationen
 * (listAll, findById, persist, deleteById, count …) ohne eigene Implementierung
 * bereit. Die Suche nach Titel wird als abgeleitete Panache-Query ergänzt.
 */
@ApplicationScoped
public class TodoRepository implements PanacheRepository<TodoEntity> {

    public List<TodoEntity> findByTitleContainingIgnoreCase(String search) {
        return list("lower(title) like ?1", "%" + search.toLowerCase() + "%");
    }

}
