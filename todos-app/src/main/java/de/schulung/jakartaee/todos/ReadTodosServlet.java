package de.schulung.jakartaee.todos;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

/**
 * Gibt die Todos als Plain Text aus. Über den optionalen Query-Parameter
 * {@code search} kann case-insensitive nach im Titel enthaltenem Text
 * gefiltert werden.
 */
@WebServlet("/todos")
public class ReadTodosServlet extends HttpServlet {

    private final TodosService todosService = TodosService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");

        String search = req.getParameter("search");
        Collection<Todo> todos;
        if(search != null && !search.trim().isEmpty()) {
        	todos = todosService.getTodos(search.trim());
        } else {
        	todos = todosService.getTodos();
        }

        try(PrintWriter out = resp.getWriter()) {
        	out.print("<h1>Todos</h1>");
        	out.print("<ul>");
            for (Todo todo : todos) {
                out.print("<li>");
                out.print("[" + todo.getStatus() + "] ");
                out.print(todo.getTitle());
                out.print(" - ");
                out.print(todo.getDescription());
                if(null != todo.getDueDate()) {
                	out.print(" (bis ");
                	out.print(todo.getDueDate());
                	out.print(")");
            	}                    
            }    
            out.print("</ul>");
        }
    }

}
