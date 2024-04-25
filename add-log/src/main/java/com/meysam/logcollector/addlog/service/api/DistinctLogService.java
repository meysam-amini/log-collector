package com.meysam.logcollector.addlog.service.api;

import com.meysam.logcollector.common.model.dto.AddLogRequestDto;
import com.meysam.logcollector.common.model.entity.LogEntity;
import com.meysam.logcollector.common.model.enums.LogStatus;

public interface DistinctLogService {

    LogEntity addLogToDbInDistinctTransaction(AddLogRequestDto addLogRequestDto, LogStatus status);
    int updateLogStatusInDistinctTransaction(long id , LogStatus status);
}
