package org.supermetrics.mapper;

import org.mapstruct.Mapper;
import org.supermetrics.dto.DeviceDTO;
import org.supermetrics.model.Device;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    DeviceDTO toDTO(Device device);

    Device toEntity(DeviceDTO dto);

    List<DeviceDTO> toDTOList(List<Device> devices);

}
