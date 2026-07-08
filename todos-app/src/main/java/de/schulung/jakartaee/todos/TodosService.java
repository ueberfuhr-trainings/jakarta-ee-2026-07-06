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
    	this.addTodo(
    			new Todo()
    				.setTitle("Einkaufen gehen")
    				.setDescription("Milch, Brot und Butter besorgen")
    				.setDueDate(LocalDate.of(2026, 7, 10))
    		);
    	this.addTodo(
        		new Todo()
        			.setTitle("Übung vorbereiten")
        			.setDescription("Servlet-Übung für die Schulung fertigstellen")
        			.setDueDate(LocalDate.of(2026, 7, 8))
        			.setStatus(TodoStatus.IN_ARBEIT)
        	);
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
    
    public void addTodo(Todo todo) {
    	todos.add(todo);
    }

}
