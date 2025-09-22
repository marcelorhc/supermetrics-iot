package com.supermetrics.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.supermetrics.dto.ReadingAggregationResponse;
import com.supermetrics.dto.ReadingRequest;
import com.supermetrics.exception.NotFoundException;
import com.supermetrics.mapper.ReadingMapper;
import com.supermetrics.model.Device;
import com.supermetrics.model.Reading;
import com.supermetrics.model.enums.DeviceCategory;
import com.supermetrics.model.enums.DeviceType;
import com.supermetrics.repository.ReadingRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadingServiceTest {

    @Mock
    private ReadingRepository readingRepository;

    @Mock
    private DeviceService deviceService;

    @Mock
    private ReadingMapper readingMapper;

    @InjectMocks
    private ReadingService readingService;

    private Device device;
    private Reading reading;
    private ReadingRequest readingRequest;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<ReadingAggregationResponse> aggregationResponses;

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

        reading = Reading.builder()
                .id("reading-id-1")
                .deviceId(device.getId())
                .value(75.0)
                .timestamp(LocalDateTime.now())
                .build();

        startTime = LocalDateTime.now().minusDays(7);
        endTime = LocalDateTime.now();

        ReadingAggregationResponse aggregationResponse = new ReadingAggregationResponse();
        aggregationResponse.setDeviceId(device.getId());
        aggregationResponse.setDeviceName(device.getName());
        aggregationResponse.setMinValue(70.0);
        aggregationResponse.setMaxValue(80.0);
        aggregationResponse.setAvgValue(75.0);
        aggregationResponse.setCount(10L);

        aggregationResponses = List.of(aggregationResponse);
    }

    @Test
    void findByDeviceCategoryWithAggregation_withValidData_shouldReturnAggregatedResults() {
        DeviceCategory category = DeviceCategory.HEALTH;
        List<String> deviceIds = List.of(device.getId());
        String zone = "Zone A";
        List<DeviceType> deviceTypes = DeviceType.getDevicesByCategory(category);
        List<Device> devices = List.of(device);

        when(deviceService.findByTypesIdsAndZone(deviceTypes, deviceIds, zone)).thenReturn(devices);
        when(readingRepository.getAllAggregations(any(), eq(startTime), eq(endTime))).thenReturn(aggregationResponses);

        List<ReadingAggregationResponse> result = readingService.findByDeviceCategoryWithAggregation(
                category, deviceIds, zone, startTime, endTime);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(device.getId(), result.get(0).getDeviceId());
        assertEquals(device.getName(), result.get(0).getDeviceName());
        verify(deviceService).findByTypesIdsAndZone(deviceTypes, deviceIds, zone);
        verify(readingRepository).getAllAggregations(any(), eq(startTime), eq(endTime));
    }

    @Test
    void findByDeviceCategoryWithAggregation_withNoDevicesFound_shouldThrowNotFoundException() {
        DeviceCategory category = DeviceCategory.HEALTH;
        List<String> deviceIds = List.of(device.getId());
        String zone = "Zone A";
        List<DeviceType> deviceTypes = DeviceType.getDevicesByCategory(category);

        when(deviceService.findByTypesIdsAndZone(deviceTypes, deviceIds, zone)).thenReturn(List.of());

        assertThrows(NotFoundException.class, () -> readingService.findByDeviceCategoryWithAggregation(
                category, deviceIds, zone, startTime, endTime));
        verify(deviceService).findByTypesIdsAndZone(deviceTypes, deviceIds, zone);
        verify(readingRepository, never()).getAllAggregations(any(), any(), any());
    }

    @Test
    void saveReading_withValidData_shouldSaveReading() {
        when(readingMapper.mapToReading(readingRequest)).thenReturn(reading);
        when(deviceService.findById(device.getId())).thenReturn(Optional.of(device));

        readingService.saveReading(readingRequest);

        verify(readingMapper).mapToReading(readingRequest);
        verify(deviceService).findById(device.getId());
        verify(readingRepository).save(reading);
    }

    @Test
    void saveReading_withNonExistingDevice_shouldThrowNotFoundException() {
        when(readingMapper.mapToReading(readingRequest)).thenReturn(reading);
        when(deviceService.findById(device.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> readingService.saveReading(readingRequest));
        verify(readingMapper).mapToReading(readingRequest);
        verify(deviceService).findById(device.getId());
        verify(readingRepository, never()).save(any());
    }
}
