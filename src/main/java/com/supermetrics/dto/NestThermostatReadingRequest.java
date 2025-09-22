package com.supermetrics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "Request object for Nest thermostat readings")
public record NestThermostatReadingRequest(
        @Schema(description = "Unique identifier of the device", example = "nest-123456")
        String device_id,

        @Schema(description = "Brand of the device", example = "Nest")
        String brand,

        @Schema(description = "Type of the device", example = "Thermostat")
        String type,

        @Schema(description = "Timestamp when the reading was collected", example = "2023-01-01T12:00:00Z")
        Instant timestamp,

        @Schema(description = "Current ambient temperature in Celsius", example = "22.5")
        double ambient_temperature_c,

        @Schema(description = "Target temperature in Celsius", example = "23.0")
        double target_temperature_c
) implements ReadingRequest {
}
