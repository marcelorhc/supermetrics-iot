package com.supermetrics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.supermetrics.dto.DeviceDTO;
import com.supermetrics.mapper.DeviceMapper;
import com.supermetrics.model.Device;
import com.supermetrics.service.DeviceService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@Tag(name = "Device Controller", description = "API endpoints for managing IoT devices")
public class DeviceController {

    private final DeviceService deviceService;
    private final DeviceMapper deviceMapper;

    @Operation(summary = "Get all devices", description = "Retrieves a list of all IoT devices")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of devices",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = DeviceDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<DeviceDTO>> getAllDevices() {
        log.info("Retrieving all devices");
        List<Device> devices = deviceService.findAll();
        return ResponseEntity.ok(deviceMapper.toDTOList(devices));
    }

    @Operation(summary = "Get device by ID", description = "Retrieves a specific device by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the device",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = DeviceDTO.class))),
        @ApiResponse(responseCode = "404", description = "Device not found",
                content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<DeviceDTO> getDeviceById(
            @Parameter(description = "ID of the device to retrieve", required = true) 
            @PathVariable String id) {
        log.info("Retrieving device with id: {}", id);
        return deviceService.findById(id)
                .map(deviceMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new device", description = "Creates a new IoT device")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Device successfully created",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = DeviceDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<DeviceDTO> createDevice(
            @Parameter(description = "Device data to create", required = true) 
            @RequestBody DeviceDTO deviceDTO) {
        log.info("Creating new device: {}", deviceDTO.name());
        Device savedDevice = deviceService.save(deviceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceMapper.toDTO(savedDevice));
    }

    @Operation(summary = "Update a device", description = "Updates an existing IoT device by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Device successfully updated",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = DeviceDTO.class))),
        @ApiResponse(responseCode = "404", description = "Device not found",
                content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<DeviceDTO> updateDevice(
            @Parameter(description = "ID of the device to update", required = true) 
            @PathVariable String id, 
            @Parameter(description = "Updated device data", required = true) 
            @RequestBody DeviceDTO deviceDTO) {
        try {
            Device updatedDevice = deviceService.update(deviceDTO);
            return ResponseEntity.ok(deviceMapper.toDTO(updatedDevice));
        } catch (RuntimeException e) {
            log.error("Error updating device: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a device", description = "Deletes an IoT device by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Device successfully deleted",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Device not found",
                content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(
            @Parameter(description = "ID of the device to delete", required = true) 
            @PathVariable String id) {
        log.info("Deleting device with id: {}", id);
        try {
            deviceService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting device: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
