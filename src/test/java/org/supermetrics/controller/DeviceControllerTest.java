package org.supermetrics.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.supermetrics.dto.DeviceDTO;
import org.supermetrics.model.Device;
import org.supermetrics.model.enums.DeviceType;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class DeviceControllerTest extends BaseControllerTest {

    @Test
    void getAllDevices_shouldReturnAllDevices() {
        Device device1 = createTestDevice("Test Device 1", "Zone A");
        Device device2 = createTestDevice("Test Device 2", "Zone A");

        givenUserAuth()
                .when()
                .get("/devices")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", greaterThanOrEqualTo(2))
                .body("name", hasItems(device1.getName(), device2.getName()));
    }

    @Test
    void getDeviceById_withValidId_shouldReturnDevice() {
        Device device = createTestDevice("Test Device", "Zone A");

        DeviceDTO deviceDTO = givenUserAuth()
                .when()
                .get("/devices/{id}", device.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().as(DeviceDTO.class);

        assertEquals(device.getId(), deviceDTO.id());
        assertEquals(device.getName(), deviceDTO.name());
        assertEquals(device.getBrand(), deviceDTO.brand());
        assertEquals(device.getSerialNumber(), deviceDTO.serialNumber());
        assertEquals(device.getType(), deviceDTO.type());
        assertEquals(device.getZone(), deviceDTO.zone());
        assertEquals(device.isActive(), deviceDTO.active());
    }

    @Test
    void getDeviceById_withInvalidId_shouldReturn404() {
        givenUserAuth()
                .when()
                .get("/devices/{id}", "nonexistent-id")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void createDevice_withValidData_shouldCreateAndReturnDevice() {
        DeviceDTO dto = new DeviceDTO(
                null,
                "New Test Device",
                "Brand",
                "Serial",
                DeviceType.HEART_RATE_MONITOR,
                "Zone A",
                true
        );

        DeviceDTO response = givenUserAuth()
                .body(dto)
                .when()
                .post("/devices")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(DeviceDTO.class);

        assertNotNull(response.id());
        assertEquals(1, deviceRepository.findById(response.id()).stream().count());
    }

    @Test
    void updateDevice_withValidData_shouldUpdateAndReturnDevice() {
        Device device = createTestDevice("Original Name", "Zone A");

        DeviceDTO dto = new DeviceDTO(
                device.getId(),
                "Updated Name",
                device.getBrand(),
                device.getSerialNumber(),
                device.getType(),
                device.getZone(),
                device.isActive()
        );

        List<Device> devicesBefore = deviceRepository.findAll();
        assertEquals(1, devicesBefore.size());

        givenUserAuth()
                .body(dto)
                .when()
                .put("/devices/{id}", device.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(device.getId()))
                .body("name", equalTo("Updated Name"));

        List<Device> devicesAfter = deviceRepository.findAll();
        assertEquals(1, devicesAfter.size());

        Device updatedDevice = deviceRepository.findById(device.getId()).orElse(null);
        assertNotNull(updatedDevice);
        assertEquals("Updated Name", updatedDevice.getName());
    }

    @Test
    void deleteDevice_withValidId_shouldMarkDeviceAsInactive() {
        Device device = createTestDevice("Device to Delete", "Zone A");

        givenUserAuth()
                .when()
                .delete("/devices/{id}", device.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        Device updatedDevice = deviceRepository.findById(device.getId()).orElse(null);
        assertNotNull(updatedDevice);
        assertFalse(updatedDevice.isActive());
    }

    @Test
    void deleteDevice_withInvalidId_shouldReturnNoContent() {
        givenUserAuth()
                .when()
                .delete("/devices/{id}", "nonexistent-id")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

}
