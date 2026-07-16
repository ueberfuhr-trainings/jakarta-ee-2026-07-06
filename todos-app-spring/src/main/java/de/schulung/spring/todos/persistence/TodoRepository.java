package de.schulung.spring.todos.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring-Data-JPA-Repository für {@link TodoEntity}. Die CRUD-Operationen
 * (findAll, findById, save, deleteById, count …) stellt Spring Data automatisch
 * bereit; die Query-Methode {@link #findByTitleContainingIgnoreCase(String)}
 * wird aus dem Methodennamen abgeleitet.
 */
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {

    List<TodoEntity> findByTitleContainingIgnoreCase(String search);

}
