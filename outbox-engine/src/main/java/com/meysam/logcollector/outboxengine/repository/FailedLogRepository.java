package com.meysam.logcollector.outboxengine.repository;

import com.meysam.logcollector.common.outbox.repository.OutboxRepository;
import com.meysam.logcollector.outboxengine.model.FailedLog;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FailedLogRepository extends OutboxRepository<FailedLog> {

    Optional<FailedLog> findByOutboxTrackingCode(int trackingCode);

}
