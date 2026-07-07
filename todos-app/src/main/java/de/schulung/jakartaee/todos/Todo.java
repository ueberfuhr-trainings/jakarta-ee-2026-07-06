package de.schulung.jakartaee.todos;

import java.time.LocalDate;

/**
 * Ein einzelnes Todo mit Titel, Beschreibung, Fälligkeitsdatum und Status.
 */
public class Todo {

    private String title;
    private String description;
    private LocalDate dueDate;
    private Status status;

    public Todo() {
    }

    public Todo(String title, String description, LocalDate dueDate, Status status) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return title
                + " (" + (status != null ? status.getLabel() : "-") + ")"
                + ", fällig am " + dueDate
                + ": " + description;
    }

}
