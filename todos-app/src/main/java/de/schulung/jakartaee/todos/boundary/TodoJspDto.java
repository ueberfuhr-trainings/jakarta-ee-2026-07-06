package de.schulung.jakartaee.todos.boundary;

import java.time.LocalDate;

/**
 * Anzeige-Repräsentation eines Todos für die Weitergabe an die JSP. Wird
 * ausschließlich lesend (bei der Anzeige) verwendet. Der Status ist hier bewusst
 * ein {@link String} – eine bereits für die Anzeige aufbereitete Bezeichnung,
 * nicht das Domänen-Enum. Die Umwandlung übernimmt der {@link TodoJspDtoMapper}.
 */
public class TodoJspDto {

    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
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
