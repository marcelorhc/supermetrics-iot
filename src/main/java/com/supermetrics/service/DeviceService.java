package com.supermetrics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.supermetrics.dto.DeviceDTO;
import com.supermetrics.exception.NotFoundException;
import com.supermetrics.mapper.DeviceMapper;
import com.supermetrics.model.Device;
import com.supermetrics.model.enums.DeviceType;
import com.supermetrics.repository.DeviceRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;

    public Device save(DeviceDTO deviceDTO) {
        log.info("Saving device: {}", deviceDTO.name());
        Device device = deviceMapper.toEntity(deviceDTO);
        return deviceRepository.save(device);
    }

    public Device update(DeviceDTO deviceDTO) {
        log.info("Updating device: {}", deviceDTO.name());
        Device device = deviceMapper.toEntity(deviceDTO);
        if (device.getId() == null || !deviceRepository.existsById(device.getId())) {
            throw new NotFoundException("Device not found with id: " + device.getId());
        }
        return deviceRepository.save(device);
    }

    public void delete(String id) {
        log.info("Performing logical delete for device with id: {}", id);
        deviceRepository.findById(id)
                .ifPresent(device -> {
                    device.setActive(false);
                    deviceRepository.save(device);
                    log.info("Device with id: {} marked as inactive", id);
                });
    }

    public Optional<Device> findById(String id) {
        return deviceRepository.findById(id);
    }

    public List<Device> findByTypesIdsAndZone(List<DeviceType> types, List<String> ids, String zone) {
        log.info("Finding devices with filters - Types: {}, Ids: {}, Zone: {}", types, ids, zone);
        return deviceRepository.findByTypesIdsAndOptionalZone(types, ids, zone);
    }

    public List<Device> findAll() {
        log.info("Finding all devices");
        return deviceRepository.findAll();
    }

    public List<Device> findAllActiveDevices() {
        log.info("Finding all active devices");
        return deviceRepository.findByActive(true);
    }

}
