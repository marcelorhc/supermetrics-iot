package com.supermetrics.repository;

import com.supermetrics.model.Device;
import com.supermetrics.model.enums.DeviceType;

import java.util.List;

public interface DeviceRepositoryCustom {

    List<Device> findByTypesIdsAndOptionalZone(List<DeviceType> types, List<String> ids, String zone);
}
