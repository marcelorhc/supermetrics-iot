package org.supermetrics.mapper;

import org.springframework.stereotype.Component;
import org.supermetrics.dto.*;
import org.supermetrics.exception.BadRequestException;
import org.supermetrics.model.Reading;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class ReadingMapper {

    private final Map<Class<? extends ReadingRequest>, Function<ReadingRequest, Reading>> mappers;

    public ReadingMapper() {
        this.mappers = new HashMap<>();
        initializeMappers();
    }

    private void initializeMappers() {
        mappers.put(AppleHeartReadingRequest.class, this::mapAppleHeartSensor);
        mappers.put(BMWFuelReadingRequest.class, this::mapBMWFuelSensor);
        mappers.put(FitbitHeartReadingRequest.class, this::mapFitbitHeartSensor);
        mappers.put(FordFuelReadingRequest.class, this::mapFordFuelSensor);
        mappers.put(HoneywellThermostatReadingRequest.class, this::mapHoneywellThermostat);
        mappers.put(NestThermostatReadingRequest.class, this::mapNestThermostat);
        mappers.put(GarminBloodOxygenReadingRequest.class, this::mapGarminBloodLevel);
    }

    public Reading mapToReading(ReadingRequest request) {
        Function<ReadingRequest, Reading> mapper = mappers.get(request.getClass());

        if (mapper == null) {
            throw new BadRequestException("Unsupported request type: " + request.getClass().getName());
        }

        return mapper.apply(request);
    }

    private Reading mapAppleHeartSensor(ReadingRequest request) {
        AppleHeartReadingRequest appleRequest = (AppleHeartReadingRequest) request;
        return Reading.builder()
                .deviceId(appleRequest.id())
                .value(appleRequest.bpm())
                .unit("bpm")
                .timestamp(appleRequest.collected_at().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .deviceReading(appleRequest)
                .build();
    }

    private Reading mapBMWFuelSensor(ReadingRequest request) {
        BMWFuelReadingRequest bmwRequest = (BMWFuelReadingRequest) request;
        return Reading.builder()
                .deviceId(bmwRequest.device_id())
                .value(bmwRequest.fuel_level_percent())
                .unit("%")
                .timestamp(bmwRequest.timestamp().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .deviceReading(bmwRequest)
                .build();
    }

    private Reading mapFitbitHeartSensor(ReadingRequest request) {
        FitbitHeartReadingRequest fitbitRequest = (FitbitHeartReadingRequest) request;
        return Reading.builder()
                .deviceId(fitbitRequest.device_id())
                .value(fitbitRequest.heartRate())
                .unit("bpm")
                .timestamp(fitbitRequest.timestamp().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .deviceReading(fitbitRequest)
                .build();
    }

    private Reading mapGarminBloodLevel(ReadingRequest request) {
        GarminBloodOxygenReadingRequest garminBloodOxygen = (GarminBloodOxygenReadingRequest) request;
        return Reading.builder()
                .deviceId(garminBloodOxygen.device_id())
                .value(garminBloodOxygen.bloodOxygenLevel())
                .unit("%")
                .timestamp(garminBloodOxygen.timestamp().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .deviceReading(garminBloodOxygen)
                .build();
    }

    private Reading mapFordFuelSensor(ReadingRequest request) {
        FordFuelReadingRequest fordRequest = (FordFuelReadingRequest) request;
        return Reading.builder()
                .deviceId(fordRequest.vehicleId())
                .value(fordRequest.fuelLiters() / fordRequest.tankCapacityLiters() * 100)
                .unit("%")
                .timestamp(fordRequest.time().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .deviceReading(request)
                .build();
    }

    private Reading mapHoneywellThermostat(ReadingRequest request) {
        HoneywellThermostatReadingRequest honeywellRequest = (HoneywellThermostatReadingRequest) request;
        return Reading.builder()
                .deviceId(honeywellRequest.id())
                .value(honeywellRequest.tempCurrent())
                .unit("C")
                .timestamp(honeywellRequest.time().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .deviceReading(request)
                .build();
    }

    private Reading mapNestThermostat(ReadingRequest request) {
        NestThermostatReadingRequest nestRequest = (NestThermostatReadingRequest) request;
        return Reading.builder()
                .deviceId(nestRequest.device_id())
                .value(nestRequest.ambient_temperature_c())
                .unit("C")
                .timestamp(nestRequest.timestamp().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .deviceReading(nestRequest)
                .build();
    }
}
