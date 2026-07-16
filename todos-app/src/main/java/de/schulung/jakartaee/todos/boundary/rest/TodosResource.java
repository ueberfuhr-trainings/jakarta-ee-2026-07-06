package de.schulung.jakartaee.todos.boundary.rest;

import java.net.URI;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.schulung.jakartaee.todos.domain.Todo;
import de.schulung.jakartaee.todos.domain.TodosService;

/**
 * REST-Ressource für Todos unter {@code /api/todos}. In dieser ersten Übung
 * bietet sie nur das Anlegen ({@code POST}); Auslesen und Löschen folgen in der
 * nächsten Übung. Ein- und Ausgabe erfolgen als JSON; die Umwandlung zwischen
 * {@link Todo} und {@link TodoDto} übernimmt der {@link TodoDtoMapper}.
 */
@Path("todos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TodosResource {

    @Inject
    TodosService todosService;
    @Inject
    TodoDtoMapper mapper;

    @POST
    public Response create(@Valid TodoDto dto, @Context UriInfo uriInfo) {
        Todo todo = mapper.toDomain(dto);
        todosService.addTodo(todo);   // setzt die generierte id in todo
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(todo.getId()))
                .build();
        return Response.created(location)
                .entity(mapper.toDto(todo))
                .build();
    }

}
