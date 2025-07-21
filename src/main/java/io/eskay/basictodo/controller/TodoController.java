package io.eskay.basictodo.controller;

import io.eskay.basictodo.dto.request.TodoRequest;
import io.eskay.basictodo.dto.response.TodoDto;
import io.eskay.basictodo.exception.ExceptionResponse;
import io.eskay.basictodo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/todos")
@Validated
@Tag(name = "Todo REST CRUD API")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    @Operation(summary = "Retrieve all todos", description = "Fetches all todos. Can accept an optional request-param" +
            " 'completed' to filter todos by their completed status")
    @GetMapping
    public ResponseEntity<List<TodoDto>> getAllTodos(
            @RequestParam(
                    value = "completed",
                    required = false
            ) @Parameter(
                    name = "completed",
                    description = "filter todos by their completed status",
                    required = false) Boolean isComplete
    ) {
        List<TodoDto> todos;
        if (isComplete == null)
            todos = todoService.getAllTodos();
        else
            todos = todoService.getAllTodosByCompletedStatus(isComplete);
        return ResponseEntity.ok(todos);
    }

    @Operation(summary = "Retrieve todo object", description = "Fetch todo with {id}")
    @GetMapping("{id}")
    public ResponseEntity<TodoDto> getTodo
            (@PathVariable
             @Parameter(
                     name = "id",
                     description = "{id} of todo object",
                     required = true) Long id) {
        var todo = todoService.getTodo(id);
        return ResponseEntity.ok(todo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create todo",
            description = "Creates a new todo object using data provided in request-body")
    public ResponseEntity<TodoDto> createTodo(
            @RequestBody
            @Valid
            TodoRequest todo) {
        var createdTodo = todoService.createTodo(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update todo object", description = "Updates the name of todo object with {id}")
    public ResponseEntity<TodoDto> updateTodo(
            @PathVariable @Parameter(
                    name = "id",
                    description = "{id} of todo to update",
                    required = true)Long id,
            @RequestParam
            @NotBlank(message = "name cannot be empty")
            @Size(min = 4, message = "name should be at least 4 chars")
                    @Parameter(name = "name",
                    description = "name to update todo with",
                    required = true)
            String name
    ) {
        var updatedTodo = todoService.updateTodo(id,name);
        return ResponseEntity.ok(updatedTodo);
    }

    @PatchMapping ("{id}")
    @Operation(summary = "Check or uncheck a todo as completed", description = "Toggles the 'completed' status of a todo with {id}")
    public ResponseEntity<TodoDto> toggleCompletedStatus(
            @PathVariable @Parameter(
                    name = "id",
                    description = "{id} of todo object",
                    required = true)Long id,
            @RequestParam(value = "completed") @Parameter(
                    name = "completed",
                    description = "Value to set 'completed' status of todo",
                    required = true)boolean isComplete) {
        var updatedTodo = todoService.toggleCompletedStatus(id,isComplete);
        return ResponseEntity.ok(updatedTodo);
    }

    @Operation(summary = "Delete todo object", description = "Deletes a todo object with {id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTodo(
            @PathVariable
            @Parameter(
                    name = "id",
                    description = "{id} of todo object",
                    required = true)
            Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
