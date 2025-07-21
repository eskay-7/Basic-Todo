package io.eskay.basictodo.dto.response;

import java.time.LocalDate;

public record TodoDto(
        Long id,
        String name,
        LocalDate created_at,
        boolean completed) {
}
