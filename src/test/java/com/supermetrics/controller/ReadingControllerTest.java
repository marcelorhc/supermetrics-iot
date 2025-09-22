package com.supermetrics.controller;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import com.supermetrics.dto.ErrorResponse;
import com.supermetrics.dto.ReadingAggregationResponse;
import com.supermetrics.model.Device;
import com.supermetrics.model.Reading;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReadingControllerTest extends BaseControllerTest {

    @Test
    void aggregate_withDeviceIdFilter_shouldReturnFilteredReadings() {
        Device device1 = createTestDevice("Test Device 1", "Test Zone");
        Device device2 = createTestDevice("Test Device 2", "Test Zone");

        createTestReading(device1, 75.5, LocalDateTime.now().minusHours(2));
        createTestReading(device2, 80.2, LocalDateTime.now().minusHours(1));

        LocalDateTime startTime = LocalDateTime.now().minusHours(3);
        LocalDateTime endTime = LocalDateTime.now();

        List<ReadingAggregationResponse> response = givenUserAuth()
                .queryParam("deviceIds", device1.getId())
                .queryParam("startTime", startTime.toString())
                .queryParam("endTime", endTime.toString())
                .when()
                .get("/readings")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("", ReadingAggregationResponse.class);

        assertEquals(1, response.size());
        for (ReadingAggregationResponse readingResponse : response) {
            assertEquals(device1.getId(), readingResponse.getDeviceId());
            assertEquals(75.5, readingResponse.getAvgValue());
            assertEquals(75.5, readingResponse.getMaxValue());
            assertEquals(75.5, readingResponse.getMinValue());
            assertEquals(1, readingResponse.getCount());
        }
    }

    @Test
    void aggregate_withDeviceInvalidIdFilter_shouldReturnError() {
        Device device1 = createTestDevice("Test Device 1", "Test Zone");

        createTestReading(device1, 75.5, LocalDateTime.now().minusHours(2));

        LocalDateTime startTime = LocalDateTime.now().minusHours(3);
        LocalDateTime endTime = LocalDateTime.now();

        ErrorResponse response = givenUserAuth()
                .queryParam("deviceIds", "WrongId")
                .queryParam("startTime", startTime.toString())
                .queryParam("endTime", endTime.toString())
                .when()
                .get("/readings")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponse.class);

        assertEquals("No devices found for the given criteria", response.message());
    }

    @Test
    void aggregate_withZoneFilter_shouldReturnFilteredReadings() {
        Device device1 = createTestDevice("Test Device 1", "Zone A");
        Device device2 = createTestDevice("Test Device 2", "Zone B");

        createTestReading(device1, 75.5, LocalDateTime.now().minusHours(2));
        createTestReading(device2, 80.2, LocalDateTime.now().minusHours(1));

        LocalDateTime startTime = LocalDateTime.now().minusHours(3);
        LocalDateTime endTime = LocalDateTime.now();

        List<ReadingAggregationResponse> response = givenUserAuth()
                .queryParam("zone", "Zone A")
                .queryParam("startTime", startTime.toString())
                .queryParam("endTime", endTime.toString())
                .when()
                .get("/readings")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("", ReadingAggregationResponse.class);

        assertEquals(1, response.size());
        for (ReadingAggregationResponse readingResponse : response) {
            assertEquals(device1.getId(), readingResponse.getDeviceId());
            assertEquals(75.5, readingResponse.getAvgValue());
            assertEquals(75.5, readingResponse.getMaxValue());
            assertEquals(75.5, readingResponse.getMinValue());
            assertEquals(1, readingResponse.getCount());
        }
    }

    @Test
    void save_withValidData_shouldSaveReading() {
        Device device = createTestDevice("Test Device", "Test Zone");

        Map<String, Object> readingData = new HashMap<>();
        readingData.put("brand", "BMW");
        readingData.put("device_id", device.getId());
        readingData.put("type", "X5");
        readingData.put("fuel_level_percent", 75);
        readingData.put("range_km", 450);
        readingData.put("timestamp", Instant.now().toString());

        givenDeviceAuth()
                .contentType(ContentType.JSON)
                .body(readingData)
                .when()
                .post("/readings")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void save_withInvalidData_shouldSaveReading() {
        Device device = createTestDevice("Test Device", "Test Zone");

        Map<String, Object> readingData = new HashMap<>();
        readingData.put("brand", "wrong brand");
        readingData.put("device_id", device.getId());
        readingData.put("type", "X5");
        readingData.put("fuel_level_percent", 75);
        readingData.put("range_km", 450);
        readingData.put("timestamp", Instant.now().toString());

        ErrorResponse response = givenDeviceAuth()
                .contentType(ContentType.JSON)
                .body(readingData)
                .when()
                .post("/readings")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponse.class);

        assertEquals("Unknown device configuration", response.message());
    }

    @Test
    void givenWrongAuth_shouldReturnUnauthorized() {
        Device device = createTestDevice("Test Device", "Test Zone");

        Map<String, Object> readingData = new HashMap<>();
        readingData.put("brand", "BMW");
        readingData.put("device_id", device.getId());
        readingData.put("type", "X5");
        readingData.put("fuel_level_percent", 75);
        readingData.put("range_km", 450);
        readingData.put("timestamp", Instant.now().toString());

        givenUserAuth()
                .contentType(ContentType.JSON)
                .body(readingData)
                .when()
                .post("/readings")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());

        LocalDateTime startTime = LocalDateTime.now().minusHours(3);
        LocalDateTime endTime = LocalDateTime.now();

        givenDeviceAuth()
                .queryParam("startTime", startTime.toString())
                .queryParam("endTime", endTime.toString())
                .when()
                .get("/readings")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void endpoints_shouldRequireAuthentication() {
        given()
                .when()
                .get("/readings")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());

        given()
                .when()
                .post("/readings")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    private Reading createTestReading(Device device, double value, LocalDateTime timestamp) {
        Reading reading = Reading.builder()
                .deviceId(device.getId())
                .value(value)
                .timestamp(timestamp)
                .build();
        return readingRepository.save(reading);
    }
}
