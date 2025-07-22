package io.eskay.basictodo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTodoRequest(
        @NotBlank(message = "name cannot be empty")
        @Size(min = 4, message = "name should be at least 4 chars")
        String name
) {
}
