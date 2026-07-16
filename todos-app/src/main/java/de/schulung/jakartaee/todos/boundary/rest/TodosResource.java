package de.schulung.jakartaee.todos.boundary.rest;

import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.schulung.jakartaee.todos.domain.Todo;
import de.schulung.jakartaee.todos.domain.TodosService;

/**
 * REST-Ressource für Todos unter {@code /api/todos}. Sie bietet das Anlegen
 * ({@code POST}), das Auslesen aller und einzelner Todos ({@code GET}) sowie das
 * Löschen ({@code DELETE}). Ein- und Ausgabe erfolgen als JSON; die Umwandlung
 * zwischen {@link Todo} und {@link TodoDto} übernimmt der {@link TodoDtoMapper}.
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

    @GET
    public Collection<TodoDto> findAll() {
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
    public void delete(@PathParam("id") long id) {
        if (!todosService.deleteTodo(id)) {
            throw new NotFoundException();              // -> 404
        }
    }

}
