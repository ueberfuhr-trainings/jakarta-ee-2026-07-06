package de.schulung.quarkus.todos.boundary.rest;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import de.schulung.quarkus.todos.domain.Todo;
import de.schulung.quarkus.todos.domain.TodosService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

/**
 * REST-Ressource für Todos unter {@code /api/todos} (Quarkus REST / JAX-RS).
 * Bietet Anlegen ({@code POST}), Auslesen aller und einzelner Todos
 * ({@code GET}) sowie Löschen ({@code DELETE}). Ein- und Ausgabe erfolgen als
 * JSON (Jackson); die Umwandlung zwischen {@link Todo} und {@link TodoDto}
 * übernimmt der {@link TodoDtoMapper}.
 */
@Path("/api/todos")
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

    @GET
    public List<TodoDto> findAll() {
        return todosService.getTodos()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GET
    @Path("{id}")
    public TodoDto findOne(@PathParam("id") long id) {
        return todosService.getTodo(id)
                .map(mapper::toDto)
                .orElseThrow(NotFoundException::new);   // -> 404
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") long id) {
        if (!todosService.deleteTodo(id)) {
            throw new NotFoundException();              // -> 404
        }
        return Response.noContent().build();            // -> 204
    }

}
