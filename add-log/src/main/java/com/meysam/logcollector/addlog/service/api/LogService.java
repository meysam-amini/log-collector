package com.meysam.logcollector.addlog.service.api;

import com.meysam.logcollector.common.model.dto.AddLogRequestDto;
import com.meysam.logcollector.common.model.dto.AddLogResponseDto;
import com.meysam.logcollector.common.model.dto.LogDto;
import com.meysam.logcollector.common.model.entity.LogEntity;
import org.springframework.http.ResponseEntity;

public interface LogService {

    AddLogResponseDto addLogToQueue(AddLogRequestDto addLogRequestDto);
    ResponseEntity<AddLogResponseDto> sendLogToExternalService(AddLogRequestDto logDto);
    void sendLogsFromKafkaConsumerToExternalService(LogDto logDto);

}
