package de.schulung.spring.todos.boundary.rest;

import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.schulung.spring.todos.domain.MaximumFuture;
import de.schulung.spring.todos.domain.Title;

import lombok.Getter;
import lombok.Setter;

/**
 * Transport-Repräsentation eines Todos für die REST-API (Request und Response).
 * Getter/Setter erzeugt Lombok.
 *
 * <ul>
 *   <li>Die {@code id} ist read-only: Sie erscheint in Antworten, wird beim
 *       Anlegen aber nicht vom Client übernommen. Die Feld-Annotation
 *       {@code @JsonProperty(access = READ_ONLY)} wirkt auch mit den von Lombok
 *       erzeugten Accessoren (Jackson wertet Feld-Annotationen mit aus).</li>
 *   <li>Der {@code status} wird als API-String {@code ready} / {@code in_progress}
 *       / {@code done} übertragen (die Umwandlung erledigt der
 *       {@link TodoDtoMapper}).</li>
 * </ul>
 */
@Getter
@Setter
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

}
