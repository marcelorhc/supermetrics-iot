package com.supermetrics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "Request object for Apple heart rate readings")
public record AppleHeartReadingRequest(
        @Schema(description = "Unique identifier of the device", example = "apple-watch-123")
        String id,

        @Schema(description = "Brand of the device", example = "Apple")
        String brand,

        @Schema(description = "Timestamp when the reading was collected", example = "2023-01-01T12:00:00Z", type = "string", format = "date-time")
        Instant collected_at,

        @Schema(description = "Heart rate in beats per minute", example = "72", type = "integer", format = "int32")
        int bpm,

        @Schema(description = "Activity during the reading", example = "Running")
        String activity
) implements ReadingRequest {
}