package io.eskay.basictodo.mapper;


import io.eskay.basictodo.dto.response.TodoDto;
import io.eskay.basictodo.entity.Todo;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TodoDtoMapper implements Function<Todo, TodoDto> {
    @Override
    public TodoDto apply(Todo todo) {
        return new TodoDto(
                todo.getId(),
                todo.getName(),
                todo.getCreatedAt(),
                todo.isCompleted()
        );
    }
}
