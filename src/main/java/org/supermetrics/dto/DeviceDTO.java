package org.supermetrics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.supermetrics.model.enums.DeviceType;

@Schema(description = "Device data transfer object")
public record DeviceDTO(
        @Schema(description = "Unique identifier of the device", example = "12345")
        String id,

        @Schema(description = "Name of the device", example = "Smart Thermostat")
        String name,

        @Schema(description = "Brand of the device", example = "SuperMetrics")
        String brand,

        @Schema(description = "Serial number of the device", example = "SN-0012345678")
        String serialNumber,

        @Schema(description = "Type of the device", example = "THERMOSTAT")
        DeviceType type,

        @Schema(description = "Zone where the device is located", example = "Living Room")
        String zone,

        @Schema(description = "Indicates whether the device is active", example = "true")
        boolean active
) {
}