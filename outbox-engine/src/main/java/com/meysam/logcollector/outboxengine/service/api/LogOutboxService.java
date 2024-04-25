package com.meysam.logcollector.outboxengine.service.api;

import com.meysam.logcollector.common.outbox.service.api.OutboxService;
import com.meysam.logcollector.outboxengine.model.FailedLog;

public interface LogOutboxService extends OutboxService<FailedLog> {

    int changeStatusToSent(int trackingCode);
    FailedLog save(FailedLog failedLog, Integer trackingCode);

}
