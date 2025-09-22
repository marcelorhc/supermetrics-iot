package com.supermetrics.simulator;

import com.supermetrics.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import com.supermetrics.model.Device;
import com.supermetrics.service.DeviceService;
import com.supermetrics.service.ReadingService;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReadingsSimulator {

    // Constants for heart rate simulation
    private static final double BASE_HEART_RATE = 75.0;
    private static final double HEART_RATE_VARIATION = 15.0;

    // Constants for fuel consumption simulation
    private static final double BASE_FUEL_CONSUMPTION = 7.5;
    private static final double FUEL_CONSUMPTION_VARIATION = 2.0;
    private static final double TANK_CAPACITY_LITERS = 60.0;
    private static final int BASE_RANGE_KM = 500;
    private static final int RANGE_VARIATION_KM = 100;

    // Constants for temperature simulation
    private static final double BASE_TEMPERATURE = 22.0;
    private static final double TEMPERATURE_VARIATION = 5.0;
    private static final double TARGET_TEMPERATURE = 21.0;
    private static final double TARGET_TEMPERATURE_VARIATION = 2.0;

    private final Random random = new Random();
    private ScheduledFuture<?> scheduledTask;

    private final DeviceSimulator deviceSimulator;
    private final ReadingService readingService;
    private final DeviceService deviceService;
    private final TaskScheduler taskScheduler;

    private List<Device> devices;

    public void start() {
        devices = deviceService.findAllActiveDevices();
        if (devices.isEmpty()) {
            devices = deviceSimulator.generateAllDevices();
        }

        log.info("Starting simulator");
        scheduledTask = taskScheduler.scheduleAtFixedRate(this::simulateAndSave, Duration.ofSeconds(1));
    }

    public void stop() {
        if (scheduledTask != null) {
            log.info("Stopping simulator");
            scheduledTask.cancel(false);
        }
    }


    protected void simulateAndSave() {
        try {
            devices.forEach(device -> {
                ReadingRequest request = generateReadings(device);
                readingService.saveReading(request);
                log.debug("Generated reading for device {}: {}", device.getName(), request);
            });
        } catch (Exception e) {
            log.error("Error generating reading", e);
        }
    }

    public ReadingRequest generateReadings(Device device) {
        return switch (device.getType()) {
            case HEART_RATE_MONITOR -> generateHeartRateReading(device);
            case FUEL_CONSUMPTION_SENSOR -> generateFuelConsumptionReading(device);
            case TEMPERATURE_SENSOR -> generateTemperatureReading(device);
            case OXYGEN_LEVEL_MONITOR -> generateOxygenLevelReading(device);
        };
    }

    private ReadingRequest generateHeartRateReading(Device device) {
        double heartRate = BASE_HEART_RATE + (random.nextDouble() * 2 - 1) * HEART_RATE_VARIATION;
        int bpm = (int) Math.round(heartRate);
        String activity = "running";

        if (device.getName().toLowerCase().contains("apple")) {
            return new AppleHeartReadingRequest(
                    device.getId(),
                    "Apple",
                    Instant.now(),
                    bpm,
                    activity
            );
        } else {
            return new FitbitHeartReadingRequest(
                    device.getId(),
                    "Fitbit",
                    "Heart Rate Monitor",
                    Instant.now(),
                    bpm,
                    activity
            );
        }
    }

    private ReadingRequest generateOxygenLevelReading(Device device) {
        int oxygenLevel = 90 + random.nextInt(11);

        return new GarminBloodOxygenReadingRequest(
                device.getId(),
                "Garmin",
                Instant.now(),
                oxygenLevel,
                "Resting"
        );
    }

    private ReadingRequest generateFuelConsumptionReading(Device device) {
        double fuelConsumption = BASE_FUEL_CONSUMPTION + (random.nextDouble() * 2 - 1) * FUEL_CONSUMPTION_VARIATION;
        fuelConsumption = Math.round(fuelConsumption * 10.0) / 10.0;

        int fuelLevelPercent = (int) Math.round(100 - (fuelConsumption / 15.0) * 100);
        fuelLevelPercent = Math.max(10, Math.min(90, fuelLevelPercent)); // Keep between 10% and 90%

        int rangeKm = (int) Math.round(BASE_RANGE_KM * (fuelLevelPercent / 100.0) +
                (random.nextDouble() * 2 - 1) * RANGE_VARIATION_KM);

        double fuelLiters = TANK_CAPACITY_LITERS * (fuelLevelPercent / 100.0);

        if (device.getName().toLowerCase().contains("bmw")) {
            return new BMWFuelReadingRequest(
                    device.getId(),
                    "BMW",
                    "Fuel Sensor",
                    Instant.now(),
                    fuelLevelPercent,
                    rangeKm
            );
        } else {
            return new FordFuelReadingRequest(
                    device.getId(),
                    "Ford",
                    "Fuel Sensor",
                    Instant.now(),
                    fuelLiters,
                    TANK_CAPACITY_LITERS,
                    rangeKm
            );
        }
    }

    private ReadingRequest generateTemperatureReading(Device device) {
        double temperature = BASE_TEMPERATURE + (random.nextDouble() * 2 - 1) * TEMPERATURE_VARIATION;
        temperature = Math.round(temperature * 10.0) / 10.0;

        double targetTemp = TARGET_TEMPERATURE + (random.nextDouble() * 2 - 1) * TARGET_TEMPERATURE_VARIATION;
        targetTemp = Math.round(targetTemp * 10.0) / 10.0;

        if (device.getName().toLowerCase().contains("nest")) {
            return new NestThermostatReadingRequest(
                    device.getId(),
                    "Nest",
                    "Thermostat",
                    Instant.now(),
                    temperature,
                    targetTemp
            );
        } else {
            return new HoneywellThermostatReadingRequest(
                    device.getId(),
                    "Honeywell",
                    "Thermostat",
                    Instant.now(),
                    temperature,
                    targetTemp
            );
        }
    }

}
