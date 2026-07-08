package de.schulung.jakartaee.todos;

import java.time.LocalDate;

/**
 * Ein einzelnes Todo mit Titel, Beschreibung, Fälligkeitsdatum und Status.
 */
public class Todo {

    private String title;
    private String description;
    private LocalDate dueDate;
    private TodoStatus status = TodoStatus.ERSTELLT;

    public Todo() {
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
