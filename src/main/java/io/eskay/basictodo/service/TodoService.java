package io.eskay.basictodo.service;

import io.eskay.basictodo.dto.request.CreateTodoRequest;
import io.eskay.basictodo.dto.request.PatchTodoRequest;
import io.eskay.basictodo.dto.request.UpdateTodoRequest;
import io.eskay.basictodo.dto.response.TodoDto;


import java.util.List;

public interface TodoService {
    List<TodoDto> getAllTodos();
    List<TodoDto> getAllTodosByCompletedStatus(boolean isComplete);
    TodoDto getTodo(Long id);
    TodoDto createTodo(CreateTodoRequest request);
    TodoDto toggleCompletedStatus(Long id, boolean isComplete);
    void deleteTodo(Long id);
    TodoDto updateTodo(Long id, String name);
    TodoDto updateTodo(UpdateTodoRequest request);
    TodoDto patchTodo(PatchTodoRequest request);

}
