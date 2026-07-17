package de.schulung.spring.todos.boundary.web;

import java.time.LocalDate;

import lombok.Data;

/**
 * Anzeige-Repräsentation eines Todos für die Weitergabe an die JSP. Der Status
 * ist hier bewusst ein bereits für die Anzeige aufbereiteter {@link String}.
 * Getter/Setter erzeugt Lombok ({@code @Data}).
 */
@Data
public class TodoJspDto {

    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private String status;

}
