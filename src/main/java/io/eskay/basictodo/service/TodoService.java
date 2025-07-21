package io.eskay.basictodo.service;

import io.eskay.basictodo.dto.request.TodoRequest;
import io.eskay.basictodo.dto.response.TodoDto;


import java.util.List;

public interface TodoService {
    List<TodoDto> getAllTodos();
    List<TodoDto> getAllTodosByCompletedStatus(boolean isComplete);
    TodoDto getTodo(Long id);
    TodoDto createTodo(TodoRequest request);
    TodoDto toggleCompletedStatus(Long id, boolean isComplete);
    void deleteTodo(Long id);
    TodoDto updateTodo(Long id, String name);

}
