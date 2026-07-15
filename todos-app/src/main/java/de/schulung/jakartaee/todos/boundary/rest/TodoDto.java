package de.schulung.jakartaee.todos.boundary.rest;

import java.time.LocalDate;

import javax.json.bind.annotation.JsonbTransient;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import de.schulung.jakartaee.todos.domain.MaximumFuture;
import de.schulung.jakartaee.todos.domain.Title;

/**
 * Transport-Repräsentation eines Todos für die REST-API (Request und Response).
 * Ein eigenes DTO, damit die interne {@code Todo}-Klasse nicht direkt nach außen
 * gegeben wird.
 *
 * <ul>
 *   <li>Die {@code id} ist read-only: Sie erscheint in Antworten, wird beim
 *       Anlegen aber nicht vom Client übernommen (siehe {@link #setId(Long)}).</li>
 *   <li>Der {@code status} wird als API-String {@code ready} / {@code in_progress}
 *       / {@code done} übertragen (die Umwandlung zum internen Enum erledigt der
 *       {@link TodoDtoMapper}).</li>
 * </ul>
 */
public class TodoDto {

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

    @JsonbTransient
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
