package de.schulung.jakartaee.todos;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * Beschafft die Todos (optional über den Query-Parameter {@code search}
 * case-insensitive nach im Titel enthaltenem Text gefiltert), stellt sie als
 * Request-Attribut bereit und leitet die Darstellung per Forward an die JSP
 * weiter. Das Servlet erzeugt selbst kein HTML mehr.
 */
@WebServlet("/todos")
public class ReadTodosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String search = req.getParameter("search");

        TodosService todosService = TodosService.getInstance(getServletContext());
        Collection<Todo> todos;
        if (search != null && !search.trim().isEmpty()) {
            todos = todosService.getTodos(search.trim());
        } else {
            todos = todosService.getTodos();
        }

        req
        	.setAttribute("todos", todos);
        req
        	.getRequestDispatcher("/WEB-INF/displayTodos.jsp")
        	.forward(req, resp);
    }

}
