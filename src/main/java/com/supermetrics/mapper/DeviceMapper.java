package com.supermetrics.mapper;

import org.mapstruct.Mapper;
import com.supermetrics.dto.DeviceDTO;
import com.supermetrics.model.Device;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    DeviceDTO toDTO(Device device);

    Device toEntity(DeviceDTO dto);

    List<DeviceDTO> toDTOList(List<Device> devices);

}
