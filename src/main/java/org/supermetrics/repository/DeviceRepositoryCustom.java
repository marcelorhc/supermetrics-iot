package org.supermetrics.repository;

import org.supermetrics.model.Device;
import org.supermetrics.model.enums.DeviceType;

import java.util.List;

public interface DeviceRepositoryCustom {

    List<Device> findByTypesIdsAndOptionalZone(List<DeviceType> types, List<String> ids, String zone);
}
