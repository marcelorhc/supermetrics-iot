package com.supermetrics.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Request object for Fitbit heart rate readings")
public record GarminBloodOxygenReadingRequest(
        @Schema(description = "Unique identifier of the device", example = "Garmin-123456")
        String device_id,

        @Schema(description = "Brand of the device", example = "Garmin")
        String brand,

        @Schema(description = "Timestamp when the reading was collected", example = "2023-01-01T12:00:00Z")
        Instant timestamp,

        @Schema(description = "Blood oxygen level in percentage", example = "95")
        int bloodOxygenLevel,

        @Schema(description = "Current activity state", example = "Resting")
        String state
) implements ReadingRequest {
}
