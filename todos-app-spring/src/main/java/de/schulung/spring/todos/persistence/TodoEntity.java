package de.schulung.spring.todos.persistence;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import de.schulung.spring.todos.domain.TodoStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * Persistenz-Repräsentation eines Todos (JPA-Entity). Trägt ausschließlich die
 * Datenbank-Details (Tabelle, Spalten, Schlüssel). Getter/Setter erzeugt Lombok;
 * bewusst kein {@code @Data}, um bei einer Entity keine problematischen
 * equals/hashCode über alle Felder zu generieren.
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
