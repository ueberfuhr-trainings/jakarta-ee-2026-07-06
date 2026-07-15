package de.schulung.jakartaee.todos.domain;

import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Ein einzelnes Todo mit Titel, Beschreibung, Fälligkeitsdatum und Status.
 *
 * <p>Reines Domänenmodell (nur fachliche Regeln, keine Persistenz-/Transport-
 * Details). Getter/Setter erzeugt Lombok
 * ({@code @Getter} und {@code @Setter}); {@code @Accessors(chain = true)} erhält die fluente
 * Setter-Schreibweise ({@code new Todo().setTitle(...).setDescription(...)}).</p>
 */
@Getter
@Setter
@Accessors(chain = true)
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

}
