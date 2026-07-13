package de.schulung.jakartaee.todos.persistence;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import de.schulung.jakartaee.todos.domain.TodoStatus;

/**
 * Persistenz-Repräsentation eines Todos (JPA-Entity). Trägt ausschließlich die
 * Datenbank-Details (Tabelle, Spalten, Schlüssel). Die fachlichen Regeln (Bean
 * Validation) liegen im Domänenmodell {@link de.schulung.jakartaee.todos.domain.Todo}.
 */
@Entity
@Table(name = "TODOS")
public class TodoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE", nullable = false, length = 100)
    private String title;

    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

    @Column(name = "DUE_DATE")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private TodoStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public TodoStatus getStatus() {
        return status;
    }

    public void setStatus(TodoStatus status) {
        this.status = status;
    }

}
