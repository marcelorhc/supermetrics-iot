package org.supermetrics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Response object for error messages")
public record ErrorResponse(
        @Schema(description = "Error message", example = "Resource not found")
        String message
) {
}
