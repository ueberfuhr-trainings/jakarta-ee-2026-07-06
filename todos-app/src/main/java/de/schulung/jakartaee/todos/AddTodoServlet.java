package de.schulung.jakartaee.todos;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Legt ein neues Todo aus den Formulardaten an und fügt es dem Service hinzu.
 * Die Eingaben werden vor dem Anlegen validiert.
 */
@WebServlet("/add-todo")
public class AddTodoServlet extends HttpServlet {

	@Inject
	TodosService todosService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String dueDateParam = req.getParameter("dueDate");

        List<String> errors = new ArrayList<>();

        String trimmedTitle = title != null ? title.trim() : "";
        if (trimmedTitle.length() < 3 || trimmedTitle.length() > 100) {
            errors.add("Der Titel muss zwischen 3 und 100 Zeichen lang sein.");
        }

        LocalDate dueDate = null;
        if (dueDateParam != null && !dueDateParam.trim().isEmpty()) {
            try {
                dueDate = LocalDate.parse(dueDateParam.trim());
                if (dueDate.isBefore(LocalDate.now())) {
                    errors.add("Die Frist muss heute oder in der Zukunft liegen.");
                }
            } catch (DateTimeParseException e) {
                errors.add("Die Frist hat kein gültiges Datumsformat.");
            }
        }

        if (!errors.isEmpty()) {
        	resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Todo ist nicht valide.");
            return;
        }
        
        todosService.addTodo(
        		new Todo()
                	.setTitle(trimmedTitle)
                	.setDescription(description)
                	.setDueDate(dueDate)
             );

        resp.sendRedirect("todos");
    }

}
