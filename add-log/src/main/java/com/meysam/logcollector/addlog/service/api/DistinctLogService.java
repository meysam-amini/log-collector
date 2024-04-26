package com.meysam.logcollector.addlog.service.api;


import com.meysam.logcollector.common.model.dtos.dto.AddLogRequestDto;
import com.meysam.logcollector.common.model.dtos.enums.OutboxEventStatus;
import com.meysam.logcollector.common.model.entities.entity.LogEntity;

public interface DistinctLogService {

    LogEntity addLogToDbInDistinctTransaction(AddLogRequestDto addLogRequestDto, OutboxEventStatus status);
    int updateLogStatusInDistinctTransaction(long id , OutboxEventStatus status);
}
