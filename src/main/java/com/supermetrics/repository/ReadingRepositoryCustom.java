package com.supermetrics.repository;

import com.supermetrics.dto.ReadingAggregationResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ReadingRepositoryCustom {

    List<ReadingAggregationResponse> getAllAggregations(List<String> deviceIds, LocalDateTime startTime, LocalDateTime endTime);
}
