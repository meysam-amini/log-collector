package com.meysam.logcollector.addlog.repository;

import com.meysam.logcollector.addlog.model.entity.LogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoLogRepository extends MongoRepository<LogDocument,Long> {
}
