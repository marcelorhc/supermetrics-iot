package com.supermetrics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TimeSeries;
import org.springframework.data.mongodb.core.timeseries.Granularity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Document(collection = "readings")
@TimeSeries(collection = "readings", timeField = "timestamp", metaField = "deviceId", granularity = Granularity.SECONDS)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndex(def = "{'deviceId': 1, 'timestamp': -1}", background = true)
@CompoundIndex(def = "{'deviceId': 1, 'timestamp': 1}", background = true)
public class Reading {
    @Id
    private String id;

    @Indexed
    private String deviceId;

    private LocalDateTime timestamp;

    private double value;
    private String unit;

    private Object deviceReading;

    private Object metadata;
}