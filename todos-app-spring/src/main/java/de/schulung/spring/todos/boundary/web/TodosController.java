package de.schulung.spring.todos.boundary.web;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import de.schulung.spring.todos.domain.Todo;
import de.schulung.spring.todos.domain.TodosService;

/**
 * Klassische, server-gerenderte UI (Gegenstück zu den Servlets der
 * Jakarta-EE-App), umgesetzt mit Spring MVC. Liefert die Todo-Liste als JSP-View
 * und verarbeitet die Formulare zum Anlegen und Löschen.
 */
@Controller
public class TodosController {

    private final TodosService todosService;
    private final TodoJspDtoMapper mapper;
    private final Validator validator;

    public TodosController(TodosService todosService, TodoJspDtoMapper mapper, Validator validator) {
        this.todosService = todosService;
        this.mapper = mapper;
        this.validator = validator;
    }

    @GetMapping("/todos")
    public String list(
    		@RequestParam(name = "search", required = false) 
    		String search, 
    		Model model
    ) {
        List<TodoJspDto> todos = ((search != null && !search.trim().isEmpty())
                ? todosService.getTodos(search.trim())
                : todosService.getTodos())
                .stream()
                .map(mapper::toJspDto)
                .collect(Collectors.toList());
        model.addAttribute("todos", todos);
        return "displayTodos";   // -> /WEB-INF/displayTodos.jsp
    }

    @PostMapping("/add-todo")
    public String add(
            @RequestParam("title")
            String title,
            @RequestParam(name = "description", required = false)
            String description,
            @RequestParam(name = "dueDate", required = false)
            String dueDateParam
    ) {

        LocalDate dueDate = null;
        if (dueDateParam != null && !dueDateParam.trim().isEmpty()) {
            try {
                dueDate = LocalDate.parse(dueDateParam.trim());
            } catch (DateTimeParseException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid duedate format.");
            }
        }

        Todo todo = new Todo()
                .setTitle(title != null ? title.trim() : "")
                .setDescription(description)
                .setDueDate(dueDate);

        Set<ConstraintViolation<Todo>> violations = validator.validate(todo);
        if (!violations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, violations.toString());
        }

        todosService.addTodo(todo);
        return "redirect:/todos";
    }

    @PostMapping("/delete-todo")
    public String delete(
    		@RequestParam("id") 
    		long id
    ) {
        todosService.deleteTodo(id);
        return "redirect:/todos";
    }

}
