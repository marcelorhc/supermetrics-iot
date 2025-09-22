package org.supermetrics.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.supermetrics.dto.ReadingAggregationResponse;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReadingRepositoryCustomImpl implements ReadingRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<ReadingAggregationResponse> getAllAggregations(List<String> deviceIds, LocalDateTime startTime, LocalDateTime endTime) {

        MatchOperation matchStage = Aggregation.match(
                Criteria.where("deviceId").in(deviceIds)
                        .and("timestamp").gte(startTime).lte(endTime)
        );

        GroupOperation groupStage = Aggregation.group("deviceId")
                .avg("value").as("avgValue")
                .max("value").as("maxValue")
                .min("value").as("minValue")
                .count().as("count");

        ProjectionOperation projectStage = Aggregation.project()
                .and("_id").as("deviceId")
                .and("avgValue").as("avgValue")
                .and("maxValue").as("maxValue")
                .and("minValue").as("minValue")
                .and("count").as("count")
                .andExclude("_id");

        Aggregation aggregation = Aggregation.newAggregation(
                matchStage,
                groupStage,
                projectStage
        );

        AggregationResults<ReadingAggregationResponse> results = mongoTemplate.aggregate(
                aggregation,
                "readings",
                ReadingAggregationResponse.class
        );
        return results.getMappedResults();
    }

}