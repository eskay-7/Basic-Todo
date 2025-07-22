package io.eskay.basictodo.controller;

import io.eskay.basictodo.dto.request.CreateTodoRequest;
import io.eskay.basictodo.dto.request.PatchTodoRequest;
import io.eskay.basictodo.dto.request.UpdateTodoRequest;
import io.eskay.basictodo.dto.response.TodoDto;
import io.eskay.basictodo.exception.ExceptionResponse;
import io.eskay.basictodo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
                    required = false,
                    schema = @Schema(type = "boolean", example = "/api/todos/?completed=false"))
            Boolean isComplete

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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = TodoDto.class))),
            @ApiResponse(responseCode = "404", description = "Todo Not Found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                            {
                              "status": 404,
                              "error": "NOT FOUND",
                              "message": "Todo with id '1' not found, check and try again",
                              "timestamp": "2025-07-22T14:05:03.043+00:00"
                            }
                            """))),
    })
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED",
                    content = @Content(schema = @Schema(implementation = TodoDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Request",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                            {
                              "status": 400,
                              "error": "BAD REQUEST",
                              "message": "name cannot be empty",
                              "timestamp": "2025-07-22T14:05:03.043+00:00"
                            }
                            """))),
    })
    public ResponseEntity<TodoDto> createTodo(
            @RequestBody
            @Valid
            CreateTodoRequest todo) {
        var createdTodo = todoService.createTodo(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }

//    @PutMapping("{id}")
//    @Operation(summary = "Update todo object", description = "Updates the name of todo object with {id}")
//    public ResponseEntity<TodoDto> updateTodo(
//            @PathVariable @Parameter(
//                    name = "id",
//                    description = "{id} of todo to update",
//                    required = true)Long id,
//            @RequestParam
//            @NotBlank(message = "name cannot be empty")
//            @Size(min = 4, message = "name should be at least 4 chars")
//                    @Parameter(name = "name",
//                    description = "name to update todo with",
//                    required = true)
//            String name
//    ) {
//        var updatedTodo = todoService.updateTodo(id,name);
//        return ResponseEntity.ok(updatedTodo);
//    }

//    @PatchMapping ("{id}")
//    @Operation(summary = "Check or uncheck a todo as completed", description = "Toggles the 'completed' status of a todo with {id}")
//    public ResponseEntity<TodoDto> toggleCompletedStatus(
//            @PathVariable @Parameter(
//                    name = "id",
//                    description = "{id} of todo object",
//                    required = true)Long id,
//            @RequestParam(value = "completed") @Parameter(
//                    name = "completed",
//                    description = "Value to set 'completed' status of todo",
//                    required = true)boolean isComplete) {
//        var updatedTodo = todoService.toggleCompletedStatus(id,isComplete);
//        return ResponseEntity.ok(updatedTodo);
//    }

    @Operation(summary = "Delete todo object", description = "Deletes a todo object with {id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content",
                    content = @Content(schema = @Schema(example = "{}"))),
            @ApiResponse(responseCode = "404", description = "Todo Not Found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                            {
                              "status": 404,
                              "error": "NOT FOUND",
                              "message": "Todo with id '1' not found, check and try again",
                              "timestamp": "2025-07-22T14:05:03.043+00:00"
                            }
                            """))),
    })
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

    @PutMapping
    @Operation(summary = "Update todo object", description = "Updates todo object using data provided in request-body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = TodoDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Request",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                            {
                              "status": 400,
                              "error": "BAD REQUEST",
                              "message": "name cannot be empty",
                              "timestamp": "2025-07-22T14:05:03.043+00:00"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "Todo Not Found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                            {
                              "status": 404,
                              "error": "NOT FOUND",
                              "message": "Todo with id '1' not found, check and try again",
                              "timestamp": "2025-07-22T14:05:03.043+00:00"
                            }
                            """))),
    })
    public ResponseEntity<TodoDto> updateTodo(@RequestBody @Valid UpdateTodoRequest request) {
        var updatedTodo = todoService.updateTodo(request);
        return ResponseEntity.ok(updatedTodo);
    }

    @PatchMapping
    @Operation(summary = "Patch todo object", description = "Updates todo object using data provided in request-body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = TodoDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Request",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                            {
                              "status": 400,
                              "error": "BAD REQUEST",
                              "message": "name must be at least 4 chars",
                              "timestamp": "2025-07-22T14:05:03.043+00:00"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "Todo Not Found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                            {
                              "status": 404,
                              "error": "NOT FOUND",
                              "message": "Todo with id '1' not found, check and try again",
                              "timestamp": "2025-07-22T14:05:03.043+00:00"
                            }
                            """))),
    })
    public ResponseEntity<TodoDto> patchTodo(@RequestBody @Valid PatchTodoRequest request) {
        var patchedTodo = todoService.patchTodo(request);
        return ResponseEntity.ok(patchedTodo);
    }
}
