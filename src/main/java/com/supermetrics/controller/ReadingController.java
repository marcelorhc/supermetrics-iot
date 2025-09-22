package com.supermetrics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.supermetrics.dto.AppleHeartReadingRequest;
import com.supermetrics.dto.ReadingRequest;
import com.supermetrics.mapper.ReadingRequestMapper;
import com.supermetrics.model.enums.DeviceCategory;
import com.supermetrics.dto.ReadingAggregationResponse;
import com.supermetrics.service.ReadingService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/readings")
@RequiredArgsConstructor
@Tag(name = "Reading Controller", description = "API endpoints for managing IoT device readings")
public class ReadingController {

    private final ReadingService readingService;
    private final ReadingRequestMapper requestMapper;

    @Operation(summary = "Aggregate readings", description = "Retrieves aggregated readings based on various filters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved aggregated readings",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ReadingAggregationResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<ReadingAggregationResponse> aggregate(
            @Parameter(description = "Filter by device category", schema = @Schema(implementation = DeviceCategory.class))
            @RequestParam(required = false) DeviceCategory category,

            @Parameter(description = "Filter by specific device IDs", example = "device-123 (Use multiple times this parameter)", array = @ArraySchema(schema = @Schema(type = "string")))
            @RequestParam(required = false) List<String> deviceIds,

            @Parameter(description = "Filter by zone where devices are located", example = "Bedroom")
            @RequestParam(required = false) String zone,

            @Parameter(description = "Start time for the reading", required = true, example = "2024-01-01T00:00:00", schema = @Schema(type = "string", format = "date-time"))
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,

            @Parameter(description = "End time for the reading", required = true, example = "2024-01-02T23:59:59", schema = @Schema(type = "string", format = "date-time"))
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        return readingService.findByDeviceCategoryWithAggregation(category, deviceIds, zone, startTime, endTime);
    }

    @Operation(summary = "Save device reading", description = "Saves a new reading from an IoT device")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reading successfully saved"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PreAuthorize("hasRole('DEVICE')")
    @PostMapping
    public void save(
            @Parameter(description = "Reading data from device, this is an open endpoint, your device must be mapped in our API like the following example.",
                    required = true, schema = @Schema(implementation = AppleHeartReadingRequest.class))
            @RequestBody Map<String, Object> request) {
        log.info("Received sensor data request: {}", request);
        ReadingRequest readingRequest = requestMapper.mapToReadingRequest(request);
        readingService.saveReading(readingRequest);
    }

}
