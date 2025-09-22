package com.supermetrics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Response object containing aggregated reading data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingAggregationResponse {
    @Schema(description = "Unique identifier of the device", example = "device-123456")
    private String deviceId;

    @Schema(description = "Name of the device", example = "Living Room Thermostat")
    private String deviceName;

    @Schema(description = "Average value of readings", example = "22.5")
    private Double avgValue;

    @Schema(description = "Maximum value of readings", example = "25.0")
    private Double maxValue;

    @Schema(description = "Minimum value of readings", example = "20.0")
    private Double minValue;

    @Schema(description = "Total count of readings", example = "24")
    private Long count;
}
