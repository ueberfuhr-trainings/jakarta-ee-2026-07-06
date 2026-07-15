package de.schulung.jakartaee.todos.domain;

import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

/**
 * Ein einzelnes Todo mit Titel, Beschreibung, Fälligkeitsdatum und Status.
 *
 * <p>Reines Domänenmodell: keine Persistenz- oder Transport-Details, nur die
 * fachlichen Regeln (Bean Validation). Die Persistenz nutzt ein eigenes
 * {@code TodoEntity}, die Web-Schicht ein eigenes {@code TodoDto}; die Umwandlung
 * übernehmen Mapper in den jeweiligen Schichten.</p>
 */
public class Todo {

    private Long id;

    @NotNull
    @Title
    private String title;

    private String description;

    @FutureOrPresent
    @MaximumFuture(3)
    private LocalDate dueDate;

    @NotNull
    private TodoStatus status = TodoStatus.ERSTELLT;

    public Todo() {
    }

    public Long getId() {
        return id;
    }

    public Todo setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Todo setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Todo setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Todo setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public TodoStatus getStatus() {
        return status;
    }

    public Todo setStatus(TodoStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return title
                + " (" + (status != null ? status.name() : "-") + ")"
                + ", fällig am " + dueDate
                + ": " + description;
    }

}
