package com.supermetrics.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.supermetrics.dto.*;
import org.springframework.stereotype.Component;
import com.supermetrics.exception.BadRequestException;
import com.supermetrics.exception.TechnicalException;

import java.util.Map;

@Component
public class ReadingRequestMapper {
    // Device key constants
    public static final String DEVICE_KEY_BMW_FUEL_SENSOR = "BMW_fuel_sensor";
    public static final String DEVICE_KEY_APPLE_HEART = "Apple_heart";
    public static final String DEVICE_KEY_NEST_THERMOSTAT = "Nest_thermostat";
    public static final String DEVICE_KEY_FORD_FUEL_SENSOR = "Ford_fuel_sensor";
    public static final String DEVICE_KEY_FITBIT_HEALTH_TRACKER = "Fitbit_health_tracker";
    public static final String DEVICE_KEY_HONEYWELL_THERMOSTAT = "Honeywell_thermostat";
    public static final String DEVICE_KEY_GARMIN_BLOOD_OXYGEN = "Garmin_blood_oxygen";

    // Brand/manufacturer constants
    public static final String BRAND_BMW = "BMW";
    public static final String BRAND_APPLE = "APPLE";
    public static final String BRAND_NEST = "NEST";
    public static final String BRAND_FORD = "FORD";
    public static final String BRAND_FITBIT = "FITBIT";
    public static final String BRAND_HONEYWELL = "HONEYWELL";
    public static final String BRAND_GARMIN = "GARMIN";

    // Request field constants
    public static final String FIELD_BRAND = "brand";
    public static final String FIELD_MANUFACTURER = "manufacturer";

    private final ObjectMapper objectMapper;
    private final Map<String, Class<? extends ReadingRequest>> deviceMappings;

    public ReadingRequestMapper() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        this.deviceMappings = Map.of(
                DEVICE_KEY_BMW_FUEL_SENSOR, BMWFuelReadingRequest.class,
                DEVICE_KEY_APPLE_HEART, AppleHeartReadingRequest.class,
                DEVICE_KEY_NEST_THERMOSTAT, NestThermostatReadingRequest.class,
                DEVICE_KEY_FORD_FUEL_SENSOR, FordFuelReadingRequest.class,
                DEVICE_KEY_FITBIT_HEALTH_TRACKER, FitbitHeartReadingRequest.class,
                DEVICE_KEY_HONEYWELL_THERMOSTAT, HoneywellThermostatReadingRequest.class,
                DEVICE_KEY_GARMIN_BLOOD_OXYGEN, GarminBloodOxygenReadingRequest.class
        );
    }

    public ReadingRequest mapToReadingRequest(Map<String, Object> request) {
        String deviceKey = buildDeviceKey(request);

        if (deviceKey == null) {
            throw new BadRequestException("Unknown device configuration");
        }

        try {
            Class<? extends ReadingRequest> targetClass = deviceMappings.get(deviceKey);
            return objectMapper.convertValue(request, targetClass);
        } catch (Exception e) {
            throw new TechnicalException("Failed to map request for device: " + deviceKey, e);
        }
    }

    private String buildDeviceKey(Map<String, Object> request) {
        String brand = (String) request.get(FIELD_BRAND);
        String manufacturer = (String) request.get(FIELD_MANUFACTURER);
        String deviceIdentifier = brand != null ? brand : manufacturer;

        return switch (deviceIdentifier.toUpperCase()) {
            case BRAND_BMW -> DEVICE_KEY_BMW_FUEL_SENSOR;
            case BRAND_APPLE -> DEVICE_KEY_APPLE_HEART;
            case BRAND_NEST -> DEVICE_KEY_NEST_THERMOSTAT;
            case BRAND_FORD -> DEVICE_KEY_FORD_FUEL_SENSOR;
            case BRAND_FITBIT -> DEVICE_KEY_FITBIT_HEALTH_TRACKER;
            case BRAND_HONEYWELL -> DEVICE_KEY_HONEYWELL_THERMOSTAT;
            case BRAND_GARMIN -> DEVICE_KEY_GARMIN_BLOOD_OXYGEN;
            default -> null;
        };
    }

}
