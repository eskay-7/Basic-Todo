package io.eskay.basictodo.mapper;

import io.eskay.basictodo.dto.request.TodoRequest;
import io.eskay.basictodo.entity.Todo;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TodoRequestMapper implements Function<TodoRequest, Todo> {
    @Override
    public Todo apply(TodoRequest request) {
        return Todo.builder()
                .name(request.name())
                .completed(false)
                .build();
    }
}
