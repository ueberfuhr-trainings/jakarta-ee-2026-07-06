package de.schulung.jakartaee.todos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Verwaltet die Todos. Vorerst nur eine In-Memory-Collection mit
 * Beispieldaten und einem Getter zum Auslesen.
 */
public class TodosService {

    private final Collection<Todo> todos = new ArrayList<>();

    public TodosService() {
        todos.add(new Todo(
                "Einkaufen gehen",
                "Milch, Brot und Butter besorgen",
                LocalDate.of(2026, 7, 10),
                Status.ERSTELLT));
        todos.add(new Todo(
                "Übung vorbereiten",
                "Servlet-Übung für die Schulung fertigstellen",
                LocalDate.of(2026, 7, 8),
                Status.IN_ARBEIT));
    }

    public Collection<Todo> getTodos() {
        return todos;
    }
    
    public Collection<Todo> getTodos(String search) {
    	return todos
    			.stream()
    			.filter(todo -> todo.getTitle().toLowerCase().contains(search.toLowerCase()))
    			.collect(Collectors.toList());
    }

}
