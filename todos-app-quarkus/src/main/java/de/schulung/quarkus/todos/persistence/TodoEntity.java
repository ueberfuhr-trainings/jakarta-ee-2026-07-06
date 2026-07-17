package de.schulung.quarkus.todos.persistence;

import java.time.LocalDate;

import de.schulung.quarkus.todos.domain.TodoStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Persistenz-Repräsentation eines Todos (JPA-Entity). Beim Panache-Repository-
 * Ansatz bleibt die Entity ein „normales" JPA-Entity (sie erbt NICHT von
 * {@code PanacheEntity}); der Datenzugriff läuft über das {@link TodoRepository}.
 * Getter/Setter erzeugt Lombok; bewusst kein {@code @Data}, um bei einer Entity
 * keine problematischen equals/hashCode über alle Felder zu generieren.
 */
@Entity
@Table(name = "TODOS")
@Getter
@Setter
public class TodoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE", nullable = false, length = 100)
    private String title;

    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

    @Column(name = "DUE_DATE")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private TodoStatus status;

}
