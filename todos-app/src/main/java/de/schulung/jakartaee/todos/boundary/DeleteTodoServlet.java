package de.schulung.jakartaee.todos.boundary;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.schulung.jakartaee.todos.domain.TodosService;

/**
 * Löscht ein Todo anhand der übergebenen {@code id} und leitet anschließend
 * zurück auf die Todo-Liste. Wird vom Löschen-Button in {@code displayTodos.jsp}
 * per POST aufgerufen.
 */
@WebServlet("/delete-todo")
public class DeleteTodoServlet extends HttpServlet {

    @Inject
    TodosService todosService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing id.");
            return;
        }

        long id;
        try {
            id = Long.parseLong(idParam.trim());
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id.");
            return;
        }

        todosService.deleteTodo(id);

        resp.sendRedirect("todos");
    }

}
