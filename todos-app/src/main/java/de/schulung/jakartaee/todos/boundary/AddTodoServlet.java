package de.schulung.jakartaee.todos.boundary;

import de.schulung.jakartaee.todos.domain.Todo;
import de.schulung.jakartaee.todos.domain.TodosService;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Set;

/**
 * Legt ein neues Todo aus den Formulardaten an und fügt es dem Service hinzu.
 * Die Eingaben werden vor dem Anlegen validiert.
 */
@WebServlet("/add-todo")
public class AddTodoServlet extends HttpServlet {

	@Inject
	TodosService todosService;
	@Inject
	Validator validator;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String dueDateParam = req.getParameter("dueDate");

        String trimmedTitle = title != null ? title.trim() : "";
        LocalDate dueDate = null;
        if (dueDateParam != null && !dueDateParam.trim().isEmpty()) {
            try {
                dueDate = LocalDate.parse(dueDateParam.trim());
            } catch (DateTimeParseException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid duedate format.");
                return;
            }
        }
        
        Todo todo = new Todo()
        		.setTitle(trimmedTitle)
        		.setDescription(description)
        		.setDueDate(dueDate);

        // Validierung
 		Set<ConstraintViolation<Todo>> violations = validator.validate(todo);
 		if(!violations.isEmpty()) {
 			resp.sendError(400, violations.toString());
 			return;
 		}
        
        todosService.addTodo(todo);

        resp.sendRedirect("todos");
    }

}
