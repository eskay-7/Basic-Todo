package io.eskay.basictodo.mapper;

import io.eskay.basictodo.dto.request.CreateTodoRequest;
import io.eskay.basictodo.entity.Todo;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TodoRequestMapper implements Function<CreateTodoRequest, Todo> {
    @Override
    public Todo apply(CreateTodoRequest request) {
        return Todo.builder()
                .name(request.name())
                .completed(false)
                .build();
    }
}
