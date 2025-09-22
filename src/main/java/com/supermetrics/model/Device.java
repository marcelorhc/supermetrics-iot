package com.supermetrics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.supermetrics.model.enums.DeviceType;

@Document(collection = "devices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {
    @Id
    private String id;

    private String name;
    private String brand;
    private String serialNumber;

    private DeviceType type;

    private String zone;
    private boolean active;
}
