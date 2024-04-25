package com.meysam.logcollector.outboxengine.repository;

import com.meysam.logcollector.common.model.entity.LogEntity;
import com.meysam.logcollector.common.outbox.repository.OutboxRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedLogRepository extends OutboxRepository<LogEntity> {


}
