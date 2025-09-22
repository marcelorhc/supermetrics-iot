package com.supermetrics.service;

import com.supermetrics.dto.DeviceDTO;
import com.supermetrics.exception.NotFoundException;
import com.supermetrics.mapper.DeviceMapper;
import com.supermetrics.model.Device;
import com.supermetrics.model.enums.DeviceType;
import com.supermetrics.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private DeviceMapper deviceMapper;

    @InjectMocks
    private DeviceService deviceService;

    private Device device;
    private DeviceDTO deviceDTO;

    @BeforeEach
    void setUp() {
        device = Device.builder()
                .id("device-id-1")
                .name("Test Device")
                .brand("Test Brand")
                .serialNumber("SN12345")
                .type(DeviceType.HEART_RATE_MONITOR)
                .zone("Zone A")
                .active(true)
                .build();

        deviceDTO = new DeviceDTO(
                "device-id-1",
                "Test Device",
                "Test Brand",
                "SN12345",
                DeviceType.HEART_RATE_MONITOR,
                "Zone A",
                true
        );
    }

    @Test
    void save_shouldSaveAndReturnDevice() {
        when(deviceMapper.toEntity(deviceDTO)).thenReturn(device);
        when(deviceRepository.save(device)).thenReturn(device);

        Device result = deviceService.save(deviceDTO);

        assertNotNull(result);
        assertEquals(device.getId(), result.getId());
        assertEquals(device.getName(), result.getName());
        verify(deviceMapper).toEntity(deviceDTO);
        verify(deviceRepository).save(device);
    }

    @Test
    void update_withExistingDevice_shouldUpdateAndReturnDevice() {
        when(deviceMapper.toEntity(deviceDTO)).thenReturn(device);
        when(deviceRepository.existsById(device.getId())).thenReturn(true);
        when(deviceRepository.save(device)).thenReturn(device);

        Device result = deviceService.update(deviceDTO);

        assertNotNull(result);
        assertEquals(device.getId(), result.getId());
        assertEquals(device.getName(), result.getName());
        verify(deviceMapper).toEntity(deviceDTO);
        verify(deviceRepository).existsById(device.getId());
        verify(deviceRepository).save(device);
    }

    @Test
    void update_withNonExistingDevice_shouldThrowNotFoundException() {
        when(deviceMapper.toEntity(deviceDTO)).thenReturn(device);
        when(deviceRepository.existsById(device.getId())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> deviceService.update(deviceDTO));
        verify(deviceMapper).toEntity(deviceDTO);
        verify(deviceRepository).existsById(device.getId());
        verify(deviceRepository, never()).save(any());
    }

    @Test
    void delete_withExistingDevice_shouldMarkDeviceAsInactive() {
        when(deviceRepository.findById(device.getId())).thenReturn(Optional.of(device));

        deviceService.delete(device.getId());

        assertFalse(device.isActive());
        verify(deviceRepository).findById(device.getId());
        verify(deviceRepository).save(device);
    }

    @Test
    void delete_withNonExistingDevice_shouldDoNothing() {
        when(deviceRepository.findById("non-existing-id")).thenReturn(Optional.empty());

        deviceService.delete("non-existing-id");

        verify(deviceRepository).findById("non-existing-id");
        verify(deviceRepository, never()).save(any());
    }

    @Test
    void findById_withExistingDevice_shouldReturnDevice() {
        when(deviceRepository.findById(device.getId())).thenReturn(Optional.of(device));

        Optional<Device> result = deviceService.findById(device.getId());

        assertTrue(result.isPresent());
        assertEquals(device.getId(), result.get().getId());
        verify(deviceRepository).findById(device.getId());
    }

    @Test
    void findById_withNonExistingDevice_shouldReturnEmptyOptional() {
        when(deviceRepository.findById("non-existing-id")).thenReturn(Optional.empty());

        Optional<Device> result = deviceService.findById("non-existing-id");

        assertFalse(result.isPresent());
        verify(deviceRepository).findById("non-existing-id");
    }

    @Test
    void findByTypesIdsAndZone_shouldReturnFilteredDevices() {
        List<DeviceType> types = List.of(DeviceType.HEART_RATE_MONITOR);
        List<String> ids = List.of("device-id-1");
        String zone = "Zone A";
        List<Device> expectedDevices = List.of(device);

        when(deviceRepository.findByTypesIdsAndOptionalZone(types, ids, zone)).thenReturn(expectedDevices);

        List<Device> result = deviceService.findByTypesIdsAndZone(types, ids, zone);

        assertEquals(expectedDevices.size(), result.size());
        assertEquals(expectedDevices.get(0).getId(), result.get(0).getId());
        verify(deviceRepository).findByTypesIdsAndOptionalZone(types, ids, zone);
    }

    @Test
    void findAll_shouldReturnAllDevices() {
        List<Device> expectedDevices = Arrays.asList(device);
        when(deviceRepository.findAll()).thenReturn(expectedDevices);

        List<Device> result = deviceService.findAll();

        assertEquals(expectedDevices.size(), result.size());
        assertEquals(expectedDevices.get(0).getId(), result.get(0).getId());
        verify(deviceRepository).findAll();
    }

    @Test
    void findAllActiveDevices_shouldReturnActiveDevices() {
        List<Device> expectedDevices = Arrays.asList(device);
        when(deviceRepository.findByActive(true)).thenReturn(expectedDevices);

        List<Device> result = deviceService.findAllActiveDevices();

        assertEquals(expectedDevices.size(), result.size());
        assertEquals(expectedDevices.get(0).getId(), result.get(0).getId());
        verify(deviceRepository).findByActive(true);
    }
}