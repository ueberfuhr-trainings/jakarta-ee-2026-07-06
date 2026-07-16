package de.schulung.quarkus.todos.boundary.rest;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.schulung.quarkus.todos.domain.MaximumFuture;
import de.schulung.quarkus.todos.domain.Title;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Transport-Repräsentation eines Todos für die REST-API (Request und Response).
 *
 * <ul>
 *   <li>Die {@code id} ist read-only: Sie erscheint in Antworten, wird beim
 *       Anlegen aber nicht vom Client übernommen
 *       ({@code @JsonProperty(access = READ_ONLY)}).</li>
 *   <li>Der {@code status} wird als API-String {@code ready} / {@code in_progress}
 *       / {@code done} übertragen (die Umwandlung erledigt der
 *       {@link TodoDtoMapper}).</li>
 * </ul>
 */
public class TodoDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    @Title
    private String title;

    private String description;

    @FutureOrPresent
    @MaximumFuture(3)
    private LocalDate dueDate;

    @Pattern(regexp = "ready|in_progress|done")
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
