package org.supermetrics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "Request object for BMW fuel readings")
public record BMWFuelReadingRequest(
        @Schema(description = "Unique identifier of the device", example = "bmw-123456")
        String device_id,

        @Schema(description = "Brand of the device", example = "BMW")
        String brand,

        @Schema(description = "Type of the device", example = "X5")
        String type,

        @Schema(description = "Timestamp when the reading was collected", example = "2023-01-01T12:00:00Z")
        Instant timestamp,

        @Schema(description = "Fuel level in percentage", example = "75")
        int fuel_level_percent,

        @Schema(description = "Remaining range in kilometers", example = "450")
        int range_km
) implements ReadingRequest {
}
