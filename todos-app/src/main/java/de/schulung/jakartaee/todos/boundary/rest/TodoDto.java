package de.schulung.jakartaee.todos.boundary.rest;

import java.time.LocalDate;

import javax.json.bind.annotation.JsonbTransient;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import de.schulung.jakartaee.todos.domain.MaximumFuture;
import de.schulung.jakartaee.todos.domain.Title;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
public class TodoDto {

	@Setter(onMethod = @__(@JsonbTransient))
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
