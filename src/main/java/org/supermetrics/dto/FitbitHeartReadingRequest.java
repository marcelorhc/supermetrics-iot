package org.supermetrics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "Request object for Fitbit heart rate readings")
public record FitbitHeartReadingRequest(
        @Schema(description = "Unique identifier of the device", example = "fitbit-123456")
        String device_id,

        @Schema(description = "Brand of the device", example = "Fitbit")
        String brand,

        @Schema(description = "Type of the device", example = "Charge 5")
        String type,

        @Schema(description = "Timestamp when the reading was collected", example = "2023-01-01T12:00:00Z")
        Instant timestamp,

        @Schema(description = "Heart rate in beats per minute", example = "72")
        int heartRate,

        @Schema(description = "Current activity state", example = "Resting")
        String state
) implements ReadingRequest {
}
