package de.schulung.jakartaee.todos;

import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Ein einzelnes Todo mit Titel, Beschreibung, Fälligkeitsdatum und Status.
 */
public class Todo {

	@NotNull
	@Size(min = 3, max = 100)
    private String title;
    private String description;
    @FutureOrPresent
    private LocalDate dueDate;
    @NotNull
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
