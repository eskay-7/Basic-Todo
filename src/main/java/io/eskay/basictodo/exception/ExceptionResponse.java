package io.eskay.basictodo.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.time.LocalDate;

public record ExceptionResponse(
        @Schema(example = "400")
        int status,
        @Schema(example = "BAD REQUEST")
        HttpStatus error,
        @Schema(example = "name cannot be empty")
        String message,
        @Schema(example = "2025-07-22T14:05:03.043+00:00")
        Timestamp timestamp
) {
}
