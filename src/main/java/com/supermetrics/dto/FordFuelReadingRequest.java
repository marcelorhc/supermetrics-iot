package com.supermetrics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "Request object for Ford fuel readings")
public record FordFuelReadingRequest(
        @Schema(description = "Unique identifier of the vehicle", example = "ford-123456")
        String vehicleId,

        @Schema(description = "Manufacturer of the vehicle", example = "Ford")
        String manufacturer,

        @Schema(description = "Type of the sensor", example = "Fuel")
        String sensorType,

        @Schema(description = "Timestamp when the reading was collected", example = "2023-01-01T12:00:00Z")
        Instant time,

        @Schema(description = "Current fuel amount in liters", example = "45.5")
        double fuelLiters,

        @Schema(description = "Total tank capacity in liters", example = "60.0")
        double tankCapacityLiters,

        @Schema(description = "Remaining range in kilometers", example = "450")
        int remainingRangeKm
) implements ReadingRequest {
}
