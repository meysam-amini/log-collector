package com.meysam.logcollector.addlog.service.impl;

import com.meysam.logcollector.addlog.service.api.DistinctLogService;
import com.meysam.logcollector.addlog.repository.LogRepository;
import com.meysam.logcollector.addlog.service.api.LogService;
import com.meysam.logcollector.common.exception.exceptions.BusinessException;
import com.meysam.logcollector.common.exception.exceptions.DataBaseException;
import com.meysam.logcollector.common.exception.exceptions.ServicesException;
import com.meysam.logcollector.common.model.dtos.dto.AddLogRequestDto;
import com.meysam.logcollector.common.model.dtos.dto.AddLogResponseDto;
import com.meysam.logcollector.common.model.dtos.dto.LogDto;
import com.meysam.logcollector.common.model.dtos.enums.OutboxEventStatus;
import com.meysam.logcollector.common.model.entities.entity.LogEntity;
import com.meysam.logcollector.common.service.feign.api.ExternalServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final ExternalServiceFeignClient externalService;
    private final DistinctLogService distinctLogService;
    private final LogRepository logRepository;
    private final KafkaTemplate<String, LogDto> kafkaTemplate;

    @Value("${spring.kafka.topics.new-log}")
    private String NEW_LOG_TOPIC;
    @Value("${spring.kafka.topics.new-log-added}")
    private String NEW_SENT_LOG_ADDED;
    @Value("${spring.kafka.topics.new-sent-log-add-failed}")
    private String NEW_SENT_LOG_ADD_FAILED;
    @Value("${spring.kafka.topics.not-sent-log-add-failed}")
    private String NOT_SENT_LOG_ADD_FAILED;


    @Override
    public AddLogResponseDto addLogToQueue(AddLogRequestDto addLogRequestDto){
        
        validateLogRequestDto(addLogRequestDto);

        LogDto logDto = new LogDto(null,
                addLogRequestDto.body(),
                addLogRequestDto.serviceName(),
                addLogRequestDto.requestId(),
                addLogRequestDto.type(),
                true);

        try {
            kafkaTemplate.send(NEW_LOG_TOPIC,logDto);
            return new AddLogResponseDto(addLogRequestDto.requestId(),true);
        }catch (Exception e){
            log.error("Couldn't send log to kafka at time:{} and we will try to send that directly to external service, log:{}, exception:{}",System.currentTimeMillis(),logDto.toString(),e);
            return sendLogToExternalService(addLogRequestDto).getBody();
        }
    }

    @Override
    public ResponseEntity<AddLogResponseDto> sendLogToExternalService(AddLogRequestDto addLogRequestDto) {
        // TODO: 29.04.24 - shouldn't validate from addLogToQueue method
        validateLogRequestDto(addLogRequestDto);
        ResponseEntity<String> response=null;
        try {
            response = externalService.sendLogToExternalApi(addLogRequestDto);
        }catch (Exception e){
            log.error("Feign connection error at time :{}",System.currentTimeMillis(),e);
            response = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("");
        }

        LogEntity logEntity;
        LogDto logDto = LogDto.builder()
                .body(addLogRequestDto.body())
                .serviceName(addLogRequestDto.serviceName())
                .requestId(addLogRequestDto.requestId())
                .type(addLogRequestDto.type())
                .build();
        if(response.getStatusCode().is2xxSuccessful()){

           logDto.setProcessed(true);

            try {
                logEntity = distinctLogService.addLogToDbInDistinctTransaction(addLogRequestDto, OutboxEventStatus.SENT);
                logDto.setId(logEntity.getId());
                try {
                    //to get consumed by syncer, and then be added to query database:
                    kafkaTemplate.send(NEW_SENT_LOG_ADDED, logDto);
                }catch (Exception kafkaException){
                    log.error("we couldn't release NEW_SENT_LOG_ADDED event and sync the sent, and added log:{} at time:{} due to kafka exception:{} we are going to persist it with SENT_ADDED_NOT_SYNCED status",logDto,System.currentTimeMillis(),kafkaException);
                    try {
                        distinctLogService.updateLogStatusInDistinctTransaction(logEntity.getId() , OutboxEventStatus.SENT_NOT_SYNCED);
                    }catch (Exception dbException){
                        log.error("After sending log to 3rd party service, we tried to send NEW_SENT_LOG_ADDED event to sync log in syncer, but not only kafka had exception, but also DB had exception:{}, time:{} , logId;{}",dbException,System.currentTimeMillis(),logEntity.getId());
                    }
                }
            }catch (DataBaseException e){
                try {
                    //for further process and persistent:
                    kafkaTemplate.send(NEW_SENT_LOG_ADD_FAILED,logDto);
                }catch (Exception e2){
                    log.error("we couldn't release event NEW_SENT_LOG_ADD_FAILED to kafka after failing to add a sent log:{}, to DB at time:{}, exception:{}",logDto,System.currentTimeMillis(),e);
                }
            }

            return ResponseEntity.ok(new AddLogResponseDto(addLogRequestDto.requestId(),true));
        }
        else{
            logDto.setProcessed(false);
            try {
                logEntity = distinctLogService.addLogToDbInDistinctTransaction(addLogRequestDto, OutboxEventStatus.UNSENT);
            }catch (BusinessException e){
                try {
                    //for further process and persistent:
                    kafkaTemplate.send(NOT_SENT_LOG_ADD_FAILED,logDto);
                }catch (Exception e2){
                    log.error("we couldn't release event not-sent-log-add-failed to kafka after failing to add a not sent log:{}, to DB at time:{}, exception:{}",logDto,System.currentTimeMillis(),e);
                }
            }
            throw new ServicesException("LOG_HAS_NOT_BEEN_SENT_TO_3RDPARTY_BUT_WE_WILL_TRY_IT_LATER",HttpStatus.SERVICE_UNAVAILABLE);
        }

    }

    @Override
    public void sendLogsFromKafkaConsumerToExternalService(LogDto logDto){
        sendLogToExternalService(new AddLogRequestDto(logDto.getBody(),
                logDto.getServiceName(),
                logDto.getRequestId(),
                logDto.getType()));
    }

    private void validateLogRequestDto(AddLogRequestDto addLogRequestDto) {
        if(Objects.isNull(addLogRequestDto.body())){
            throw new BusinessException("lOG_BODY_IS_EMPTY");
        }
        if(addLogRequestDto.body().isBlank()){
            throw new BusinessException("lOG_BODY_IS_EMPTY");
        }
        if(Objects.isNull(addLogRequestDto.requestId())){
            throw new BusinessException("LOG_REQUEST_ID_IS_EMPTY");
        }
        if(addLogRequestDto.requestId().isBlank()){
            throw new BusinessException("LOG_REQUEST_ID_IS_EMPTY");
        }
    }
}
