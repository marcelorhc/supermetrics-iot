package com.supermetrics.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum DeviceType {
    // Environmental sensors
    TEMPERATURE_SENSOR(DeviceCategory.ENVIRONMENTAL),

    // Health sensors
    HEART_RATE_MONITOR(DeviceCategory.HEALTH),
    OXYGEN_LEVEL_MONITOR(DeviceCategory.HEALTH),

    // Vehicle sensors
    FUEL_CONSUMPTION_SENSOR(DeviceCategory.VEHICLE);

    private final DeviceCategory category;

    DeviceType(DeviceCategory category) {
        this.category = category;
    }

    public static List<DeviceType> getDevicesByCategory(DeviceCategory category) {
        return Arrays.stream(values())
                .filter(device -> device.getCategory() == category)
                .toList();
    }

}
