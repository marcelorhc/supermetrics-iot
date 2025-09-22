package org.supermetrics.repository;

import org.supermetrics.dto.ReadingAggregationResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ReadingRepositoryCustom {

    List<ReadingAggregationResponse> getAllAggregations(List<String> deviceIds, LocalDateTime startTime, LocalDateTime endTime);
}
