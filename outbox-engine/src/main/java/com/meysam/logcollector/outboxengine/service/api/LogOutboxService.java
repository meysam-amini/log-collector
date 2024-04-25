package com.meysam.logcollector.outboxengine.service.api;

import com.meysam.logcollector.common.model.entity.LogEntity;
import com.meysam.logcollector.common.outbox.service.api.OutboxService;

public interface LogOutboxService extends OutboxService<LogEntity> {

    int changeStatusToSent(int trackingCode);
    LogEntity save(LogEntity failedLog, Integer trackingCode);

}
