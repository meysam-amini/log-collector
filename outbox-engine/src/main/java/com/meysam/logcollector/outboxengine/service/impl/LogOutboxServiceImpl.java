package com.meysam.logcollector.outboxengine.service.impl;

import com.meysam.logcollector.common.exception.exceptions.ServicesException;
import com.meysam.logcollector.common.model.dtos.dto.AddLogRequestDto;
import com.meysam.logcollector.common.model.dtos.dto.LogDto;
import com.meysam.logcollector.common.model.dtos.enums.OutboxEventStatus;
import com.meysam.logcollector.common.model.entities.entity.LogEntity;
import com.meysam.logcollector.common.outbox.service.Impl.OutboxServiceImpl;
import com.meysam.logcollector.common.service.feign.api.ExternalServiceFeignClient;
import com.meysam.logcollector.outboxengine.repository.FailedLogRepository;
import com.meysam.logcollector.outboxengine.service.api.LogOutboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Slf4j
@Service
public class LogOutboxServiceImpl extends OutboxServiceImpl<LogEntity> implements LogOutboxService {

    private final FailedLogRepository failedLogRepository;
    private final ExternalServiceFeignClient externalService;
    private final KafkaTemplate<String, LogDto> kafkaTemplate;

    public LogOutboxServiceImpl(FailedLogRepository failedLogRepository, ExternalServiceFeignClient externalService, KafkaTemplate<String, LogDto> kafkaTemplate) {
        super(failedLogRepository);
        this.failedLogRepository = failedLogRepository;
        this.externalService = externalService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Value("${spring.kafka.topics.new-log-added}")
    private String NEW_SENT_LOG_ADDED;

    @Override
    public void process() {
        super.process();
    }

    @Override
    public void retry(LogEntity failedLog) {
        sendLogToExternalServiceFromOutbox(failedLog);
    }

    private void sendLogToExternalServiceFromOutbox(LogEntity logEntity) {
        ResponseEntity<String> response=null;
        try {
            response =
                    externalService.sendLogToExternalApi(new AddLogRequestDto(logEntity.getBody(),
                            logEntity.getServiceName(),
                            logEntity.getRequestId(),
                            logEntity.getType()));

        }catch (Exception e){
            log.error("Feign connection error at time :{}",System.currentTimeMillis(),e);
            try {
                failedLogRepository.updateStatusInDistinctTransaction(logEntity.getId(), OutboxEventStatus.UNSENT, OutboxEventStatus.getAllValidStatusesForUnsent());
            } catch (Exception dbException) {
                log.error("retry sending log:{} wasn't successful at time:{}, we couldn't update OutboxEventStatus to UNSENT again, exception:{}",
                        logEntity.toString(), System.currentTimeMillis(), dbException);
            }
            response = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("");
        }

            if (response.getStatusCode().is2xxSuccessful()) {
                try {
                    failedLogRepository.updateStatusInDistinctTransaction(logEntity.getId(), OutboxEventStatus.SENT, OutboxEventStatus.getAllValidStatusesForSent());

                    try {
                        //to get consumed by syncer, and then be added to query database:
                        kafkaTemplate.send(NEW_SENT_LOG_ADDED, new LogDto(logEntity.getId(), logEntity.getBody(),
                                logEntity.getServiceName(), logEntity.getRequestId(), logEntity.getType(), logEntity.isProcessed()));
                    } catch (Exception kafkaException) {
                        log.error("we couldn't release NEW_SENT_LOG_ADDED event and sync the sent, and added log:{} at time:{} due to kafka exception:{} we are going to persist it with SENT_ADDED_NOT_SYNCED status", logEntity, System.currentTimeMillis(), kafkaException);
                        try {
                            failedLogRepository.updateStatusInDistinctTransaction(logEntity.getId(), OutboxEventStatus.SENT_NOT_SYNCED, OutboxEventStatus.getAllValidStatusesForSentNotSynced());
                        } catch (Exception dbException) {
                            log.error("After sending log to 3rd party service, we tried to send NEW_SENT_LOG_ADDED event to sync log in syncer, but not only kafka had exception, but also DB had exception:{}, time:{} , logId;{}", dbException, System.currentTimeMillis(), logEntity.getId());
                        }
                    }

                } catch (Exception dbException) {
                    log.error("after sending log:{} successfully at time:{}, we couldn't update OutboxEventStatus to SENT, exception:{}",
                            logEntity.toString(), System.currentTimeMillis(), dbException);
                }
            } else {
                try {
                    failedLogRepository.updateStatusInDistinctTransaction(logEntity.getId(), OutboxEventStatus.UNSENT, OutboxEventStatus.getAllValidStatusesForUnsent());
                } catch (Exception dbException) {
                    log.error("retry sending log:{} wasn't successful at time:{}, we couldn't update OutboxEventStatus to UNSENT again, exception:{}",
                            logEntity.toString(), System.currentTimeMillis(), dbException);
                }
            }

        }
    }
