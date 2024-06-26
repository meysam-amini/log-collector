package com.meysam.logcollector.common.outbox.service.api;

import com.meysam.logcollector.common.model.dtos.enums.OutboxEventStatus;
import com.meysam.logcollector.common.model.entities.entity.OutBoxableBaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface OutboxService<T extends OutBoxableBaseEntity> {

    void process();

    Page<T> findAllFailed(Pageable pageable);

    int updateStatusInDistinctTransaction (Long id, OutboxEventStatus status, List<OutboxEventStatus> validStatuses);

    void retry(T t);
}
