package io.eskay.basictodo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateTodoRequest(
        @NotNull(message = "id cannot be empty")
        @Min(value = 1, message = "id must be greater than or equal to 1")
        Long id,

        @NotBlank(message = "name cannot be empty")
        @Size(min = 4, message = "name should be at least 4 chars")
        String name,

        @NotNull(message = "completed status cannot be empty")
        Boolean completed
) {
}
