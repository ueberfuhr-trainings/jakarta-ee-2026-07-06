package de.schulung.jakartaee.todos.boundary;

import de.schulung.jakartaee.todos.domain.Todo;
import de.schulung.jakartaee.todos.domain.TodosService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Beschafft die Todos (optional über den Query-Parameter {@code search}
 * case-insensitive nach im Titel enthaltenem Text gefiltert), wandelt sie in
 * {@link TodoDto}s um, stellt sie als Request-Attribut bereit und leitet die
 * Darstellung per Forward an die JSP weiter. Die JSP sieht damit nur das DTO der
 * Web-Schicht, nicht das Domänenmodell.
 */
@WebServlet("/todos")
public class ReadTodosServlet extends HttpServlet {

	@Inject
	TodosService todosService;
	@Inject
	TodoJspDtoMapper mapper;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String search = req.getParameter("search");

        Collection<Todo> todos;
        if (search != null && !search.trim().isEmpty()) {
            todos = todosService.getTodos(search.trim());
        } else {
            todos = todosService.getTodos();
        }

        List<TodoJspDto> todoDtos = todos.stream()
                .map(mapper::toJspDto)
                .collect(Collectors.toList());

        req
        	.setAttribute("todos", todoDtos);
        req
        	.getRequestDispatcher("/WEB-INF/displayTodos.jsp")
        	.forward(req, resp);
    }

}
