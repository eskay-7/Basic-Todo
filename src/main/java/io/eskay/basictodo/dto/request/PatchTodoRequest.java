package io.eskay.basictodo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PatchTodoRequest(
        @NotNull(message = "id cannot be empty")
        @Min(value = 1, message = "id must be greater than or equal to 1")
        Long id,

        String name,

        Boolean completed
) {
}
