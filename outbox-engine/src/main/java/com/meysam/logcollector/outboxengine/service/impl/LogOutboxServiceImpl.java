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
    public LogEntity save(LogEntity flog, Integer outboxTrackingCode) {
        try {
            LogEntity failedLog;
            if (Objects.isNull(outboxTrackingCode)) {
                outboxTrackingCode = (UUID.randomUUID() + "" + System.currentTimeMillis()).hashCode();
                failedLog = LogEntity.builder()
                        .body(flog.getBody())
                        .type(flog.getType())
                        .serviceName(flog.getServiceName())
                        .requestId(flog.getRequestId())
                        .processed(flog.isProcessed())
                        .status(flog.getStatus())
                        .build();
                failedLog.setOutboxTrackingCode(outboxTrackingCode);
                failedLog.setRetryCount(0);
            }else {
                failedLog = failedLogRepository.findByOutboxTrackingCode(outboxTrackingCode).orElse(null);
                if(Objects.isNull(flog)) {
                    LogOutboxServiceImpl.log.error("On saving new failed notification with outbox tracking code:{}, we couldn't find related record at time :{}",outboxTrackingCode,System.currentTimeMillis());
                    return null;
                }
            }
            failedLog.setStatus(OutboxEventStatus.UNSENT);
            failedLog.setCreatedDate(new Date());
            return failedLogRepository.save(flog);

        }catch(Exception e){
                log.error("exception on saving failed log to outbox at time:{} , data was: {}, exception is:{}", System.currentTimeMillis(), flog.toString(), e);
                return null;
            }
    }

    @Override
    public void process() {
        super.process();
    }

    @Override
    public void retry(LogEntity failedLog) {
        sendLogToExternalServiceFromOutbox(failedLog);
    }

    private void sendLogToExternalServiceFromOutbox(LogEntity logEntity) {
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
        }

    }

    public int changeStatusToSent(int outboxTrackingCode) {
        try {
            LogEntity failedLog = failedLogRepository.findByOutboxTrackingCode(outboxTrackingCode).orElse(null);
            if (Objects.nonNull(failedLog)) {
                return failedLogRepository.updateStatusInDistinctTransactionAndCountRetry(failedLog.getId(), OutboxEventStatus.SENT, OutboxEventStatus.getAllValidStatusesForSent());
            }
            return 0;
        }catch (Exception e){
            log.error("On changing status of failed log with outbox tracking code:{}, exception occurred at time:{}, exception is:{}",outboxTrackingCode,System.currentTimeMillis(),e);
            return 0;
        }
    }
}
