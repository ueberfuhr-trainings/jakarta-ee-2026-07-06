package de.schulung.jakartaee.todos.boundary;

import java.time.LocalDate;

import lombok.Data;

/**
 * Anzeige-Repräsentation eines Todos für die Weitergabe an die JSP. Wird
 * ausschließlich lesend (bei der Anzeige) verwendet. Der Status ist hier bewusst
 * ein {@link String} – eine bereits für die Anzeige aufbereitete Bezeichnung,
 * nicht das Domänen-Enum. Getter/Setter erzeugt Lombok ({@code @Data}).
 */
@Data
public class TodoJspDto {

    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private String status;

}
