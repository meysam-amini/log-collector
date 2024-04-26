package com.meysam.logcollector.addlog.service.api;

import com.meysam.logcollector.common.model.dtos.dto.AddLogRequestDto;
import com.meysam.logcollector.common.model.dtos.dto.AddLogResponseDto;
import com.meysam.logcollector.common.model.dtos.dto.LogDto;
import org.springframework.http.ResponseEntity;

public interface LogService {

    AddLogResponseDto addLogToQueue(AddLogRequestDto addLogRequestDto);
    ResponseEntity<AddLogResponseDto> sendLogToExternalService(AddLogRequestDto logDto);
    void sendLogsFromKafkaConsumerToExternalService(LogDto logDto);

}
