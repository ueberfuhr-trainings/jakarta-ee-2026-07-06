package de.schulung.spring.todos.boundary.rest;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import de.schulung.spring.todos.domain.Todo;
import de.schulung.spring.todos.domain.TodosService;

import lombok.RequiredArgsConstructor;

/**
 * REST-Ressource für Todos unter {@code /api/todos}. Umgesetzt mit Spring MVC
 * ({@code @RestController}). Bietet Anlegen ({@code POST}), Auslesen aller und
 * einzelner Todos ({@code GET}) sowie Löschen ({@code DELETE}). Ein- und Ausgabe
 * erfolgen als JSON (Jackson); die Umwandlung zwischen {@link Todo} und
 * {@link TodoDto} übernimmt der {@link TodoDtoMapper}. Den Konstruktor für die
 * {@code final}-Felder erzeugt Lombok ({@code @RequiredArgsConstructor}).
 */
@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodosRestController {

    private final TodosService todosService;
    private final TodoDtoMapper mapper;

    @PostMapping
    public ResponseEntity<TodoDto> create(
    		@Valid 
    		@RequestBody 
    		TodoDto dto
    ) {
        Todo todo = mapper.toDomain(dto);
        todosService.addTodo(todo);   // setzt die generierte id in todo
        URI location = ServletUriComponentsBuilder
        		.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(todo.getId())
                .toUri();
        return ResponseEntity
        		.created(location)
        		.body(mapper.toDto(todo));
    }

    @GetMapping
    public List<TodoDto> findAll() {
        return todosService
        		.getTodos()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TodoDto findOne(@PathVariable long id) {
        return todosService
        		.getTodo(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        if (!todosService.deleteTodo(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
