package com.supermetrics.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.supermetrics.model.Device;

import java.util.List;

@Repository
public interface DeviceRepository extends MongoRepository<Device, String>, DeviceRepositoryCustom {

    List<Device> findByActive(boolean active);

}
