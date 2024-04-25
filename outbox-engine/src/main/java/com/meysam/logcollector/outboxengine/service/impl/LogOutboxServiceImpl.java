package com.meysam.logcollector.outboxengine.service.impl;

import com.meysam.logcollector.common.model.dto.AddLogRequestDto;
import com.meysam.logcollector.common.model.entity.LogEntity;
import com.meysam.logcollector.common.model.enums.OutboxEventStatus;
import com.meysam.logcollector.common.outbox.service.Impl.OutboxServiceImpl;
import com.meysam.logcollector.common.service.feign.api.ExternalServiceFeignClient;
import com.meysam.logcollector.outboxengine.repository.FailedLogRepository;
import com.meysam.logcollector.outboxengine.service.api.LogOutboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;


@Slf4j
@Service
public class LogOutboxServiceImpl extends OutboxServiceImpl<LogEntity> implements LogOutboxService {

    private final FailedLogRepository failedLogRepository;
    private final ExternalServiceFeignClient externalService;

    public LogOutboxServiceImpl(FailedLogRepository failedLogRepository, ExternalServiceFeignClient externalService) {
        super(failedLogRepository);
        this.failedLogRepository =failedLogRepository;
        this.externalService = externalService;
    }

    @Override
    public void process() {
        super.process();
    }

    @Override
    public void retry(LogEntity failedLog) {
        sendLogToExternalServiceFromOutbox(failedLog);
    }

    @Override
    public void sendLogToExternalServiceFromOutbox(LogEntity logEntity) {
        ResponseEntity<String> response =  externalService.sendLogToExternalApi(new AddLogRequestDto(logEntity.getBody(),
                logEntity.getServiceName(),
                logEntity.getRequestId(),
                logEntity.getType()));

        if(response.getStatusCode().is2xxSuccessful()){
            try {
                failedLogRepository.updateStatusInDistinctTransaction(logEntity.getId() , OutboxEventStatus.SENT,OutboxEventStatus.getAllValidStatusesForSent());
            }catch (Exception dbException){
                log.error("after sending log:{} successfully at time:{}, we couldn't update OutboxEventStatus to SENT, exception:{}",
                        logEntity.toString(),System.currentTimeMillis(),dbException);
            }
        }else{
            try {
                failedLogRepository.updateStatusInDistinctTransaction(logEntity.getId() , OutboxEventStatus.UNSENT,OutboxEventStatus.getAllValidStatusesForUnsent());
            }catch (Exception dbException){
                log.error("retry sending log:{} wasn't successful at time:{}, we couldn't update OutboxEventStatus to UNSENT again, exception:{}",
                        logEntity.toString(),System.currentTimeMillis(),dbException);
            }
        }

    }
}
