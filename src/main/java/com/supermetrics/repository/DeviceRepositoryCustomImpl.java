package com.supermetrics.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import com.supermetrics.model.Device;
import com.supermetrics.model.enums.DeviceType;

import java.util.List;

@Repository
public class DeviceRepositoryCustomImpl implements DeviceRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Device> findByTypesIdsAndOptionalZone(List<DeviceType> types, List<String> ids, String zone) {
        Query query = new Query();
        if (types != null && !types.isEmpty()) {
            query.addCriteria(Criteria.where("type").in(types));
        }

        if (ids != null && !ids.isEmpty()) {
            query.addCriteria(Criteria.where("_id").in(ids));
        }

        if (zone != null) {
            query.addCriteria(Criteria.where("zone").is(zone));
        }

        query.addCriteria(Criteria.where("active").is(true));

        return mongoTemplate.find(query, Device.class);
    }
}
