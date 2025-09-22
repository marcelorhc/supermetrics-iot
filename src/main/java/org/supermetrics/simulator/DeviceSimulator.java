package org.supermetrics.simulator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.supermetrics.dto.DeviceDTO;
import org.supermetrics.model.Device;
import org.supermetrics.service.DeviceService;

import java.util.List;

import static org.supermetrics.model.enums.DeviceType.*;

@Service
@RequiredArgsConstructor
public class DeviceSimulator {

    private final DeviceService deviceService;

    public List<Device> generateAllDevices() {
        List<DeviceDTO> devices = List.of(
                generateAppleHeartDevice(),
                generateFitbitHeartDevice(),
                generateBMWFuelSensorDevice(),
                generateFordFuelSensorDevice(),
                generateNestThermostatDevice(),
                generateHoneywellThermostatDevice(),
                generateGarminBloodLevelDevice()
        );

        return devices.stream()
                .map(deviceService::save)
                .toList();
    }

    public DeviceDTO generateAppleHeartDevice() {
        return new DeviceDTO(
                null,
                "Apple Heart Device",
                "Apple",
                "SN-" + System.currentTimeMillis(),
                HEART_RATE_MONITOR,
                "Zone 1",
                true
        );
    }

    public DeviceDTO generateFitbitHeartDevice() {
        return new DeviceDTO(
                null,
                "Fitbit Heart Device",
                "Fitbit",
                "SN-" + System.currentTimeMillis(),
                HEART_RATE_MONITOR,
                "Zone 2",
                true
        );
    }

    public DeviceDTO generateGarminBloodLevelDevice() {
        return new DeviceDTO(
                null,
                "Garmin Speed runner",
                "Garmin",
                "G-" + System.currentTimeMillis(),
                OXYGEN_LEVEL_MONITOR,
                "Zone 2",
                true
        );
    }

    public DeviceDTO generateBMWFuelSensorDevice() {
        return new DeviceDTO(
                null,
                "BMW Fuel Sensor",
                "BMW",
                "SN-" + System.currentTimeMillis(),
                FUEL_CONSUMPTION_SENSOR,
                "Zone 3",
                true
        );
    }

    public DeviceDTO generateFordFuelSensorDevice() {
        return new DeviceDTO(
                null,
                "Ford Fuel Sensor",
                "Ford",
                "SN-" + System.currentTimeMillis(),
                FUEL_CONSUMPTION_SENSOR,
                "Zone 4",
                true
        );
    }

    public DeviceDTO generateNestThermostatDevice() {
        return new DeviceDTO(
                null,
                "Nest Thermostat",
                "Nest",
                "SN-" + System.currentTimeMillis(),
                TEMPERATURE_SENSOR,
                "Zone 5",
                true
        );
    }

    public DeviceDTO generateHoneywellThermostatDevice() {
        return new DeviceDTO(
                null,
                "Honeywell Thermostat",
                "Honeywell",
                "SN-" + System.currentTimeMillis(),
                TEMPERATURE_SENSOR,
                "Zone 6",
                true);
    }


}
