package org.supermetrics.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.supermetrics.model.Reading;

@Repository
public interface ReadingRepository extends MongoRepository<Reading, String>, ReadingRepositoryCustom {

}
