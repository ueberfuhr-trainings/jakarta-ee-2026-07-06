package de.schulung.jakartaee.todos.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

/**
 * Ein einzelnes Todo mit Titel, Beschreibung, Fälligkeitsdatum und Status.
 */
@Entity(name = "Todo")
@Table(name = "TODOS")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

	@NotNull
	@Title
    @Column(name = "TITLE", nullable = false, length = 100)
    private String title;
    @Column(name = "DESCRIPTION", length = 1000)
    private String description;
    @FutureOrPresent
    @MaximumFuture(3)
    @Column(name = "DUE_DATE")
    private LocalDate dueDate;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private TodoStatus status = TodoStatus.ERSTELLT;

    public Todo() {
    }

    public Long getId() {
        return id;
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
