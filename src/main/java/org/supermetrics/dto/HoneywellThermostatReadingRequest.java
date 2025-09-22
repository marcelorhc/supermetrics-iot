package org.supermetrics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "Request object for Honeywell thermostat readings")
public record HoneywellThermostatReadingRequest(
        @Schema(description = "Unique identifier of the device", example = "honeywell-123456")
        String id,

        @Schema(description = "Manufacturer of the device", example = "Honeywell")
        String manufacturer,

        @Schema(description = "Category of the device", example = "Thermostat")
        String category,

        @Schema(description = "Timestamp when the reading was collected", example = "2023-01-01T12:00:00Z")
        Instant time,

        @Schema(description = "Current temperature in Celsius", example = "22.5")
        double tempCurrent,

        @Schema(description = "Target temperature in Celsius", example = "23.0")
        double tempTarget
) implements ReadingRequest {
}
