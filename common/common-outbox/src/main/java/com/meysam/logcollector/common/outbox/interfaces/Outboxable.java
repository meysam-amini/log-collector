package com.meysam.logcollector.common.outbox.interfaces;

import com.meysam.logcollector.common.model.enums.OutboxEventStatus;

import java.util.Date;

public interface Outboxable {

    Integer getRetryCount();
    void setRetryCount();
    OutboxEventStatus getOutboxEventStatus();
    void setOutboxEventStatus();
    Integer getOutboxTrackingCode();
    void setOutboxTrackingCode();
    Date getCreatedDate();
    void setCreatedDate();
}
