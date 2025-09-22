package org.supermetrics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.supermetrics.dto.ReadingAggregationResponse;
import org.supermetrics.dto.ReadingRequest;
import org.supermetrics.exception.NotFoundException;
import org.supermetrics.mapper.ReadingMapper;
import org.supermetrics.model.Device;
import org.supermetrics.model.Reading;
import org.supermetrics.model.enums.DeviceCategory;
import org.supermetrics.model.enums.DeviceType;
import org.supermetrics.repository.ReadingRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReadingService {

    private final ReadingRepository readingRepository;
    private final DeviceService deviceService;
    private final ReadingMapper readingMapper;

    public List<ReadingAggregationResponse> findByDeviceCategoryWithAggregation(
            DeviceCategory category,
            List<String> deviceIds,
            String zone,
            LocalDateTime startTime,
            LocalDateTime endTime) {

        List<DeviceType> deviceTypes = DeviceType.getDevicesByCategory(category);
        List<Device> devices = deviceService.findByTypesIdsAndZone(deviceTypes, deviceIds, zone);

        if (devices.isEmpty()) {
            throw new NotFoundException("No devices found for the given criteria");
        }

        Map<String, Device> deviceMap = devices.stream()
                .collect(Collectors.toMap(Device::getId, Function.identity()));

        List<ReadingAggregationResponse> results = readingRepository
                .getAllAggregations(new ArrayList<>(deviceMap.keySet()), startTime, endTime);

        return filterAndSortAggregationResults(results, deviceMap);
    }

    private List<ReadingAggregationResponse> filterAndSortAggregationResults(List<ReadingAggregationResponse> results, Map<String, Device> deviceMap) {
        results.forEach(result ->
                Optional.ofNullable(deviceMap.get(result.getDeviceId()))
                        .ifPresent(device -> result.setDeviceName(device.getName())));

        return results.stream()
                .sorted(Comparator.comparing(ReadingAggregationResponse::getDeviceName))
                .toList();
    }

    public void saveReading(ReadingRequest request) {
        Reading reading = readingMapper.mapToReading(request);

        deviceService.findById(reading.getDeviceId())
                .orElseThrow(() -> new NotFoundException("Device not found: " + reading.getId()));

        readingRepository.save(reading);
    }

}
