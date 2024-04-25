package com.meysam.logcollector.outboxengine.service.impl;

import com.meysam.logcollector.common.outbox.model.enums.OutboxEventStatus;
import com.meysam.logcollector.common.outbox.service.Impl.OutboxServiceImpl;
import com.meysam.logcollector.outboxengine.model.FailedLog;
import com.meysam.logcollector.outboxengine.repository.FailedLogRepository;
import com.meysam.logcollector.outboxengine.service.api.LogOutboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;


@Slf4j
@Service
public class LogOutboxServiceImpl extends OutboxServiceImpl<FailedLog> implements LogOutboxService {

    private final FailedLogRepository failedLogRepository;
    public LogOutboxServiceImpl(FailedLogRepository failedLogRepository) {
        super(failedLogRepository);
        this.failedLogRepository =failedLogRepository;
    }


    @Override
    public FailedLog save(FailedLog flog, Integer outboxTrackingCode) {
        try {
            FailedLog failedLog;
            if (Objects.isNull(outboxTrackingCode)) {
                outboxTrackingCode = (UUID.randomUUID() + "" + System.currentTimeMillis()).hashCode();
                flog = FailedLog.builder()
                        .body(flog.getBody())
                        .type(flog.getType())
                        .serviceName(flog.getServiceName())
                        .requestId(flog.getRequestId())
                        .processed(flog.isProcessed())
                        .status(flog.getStatus())
                        .build();
                flog.setOutboxTrackingCode(outboxTrackingCode);
                flog.setRetryCount(0);
            }else {
                flog = failedLogRepository.findByOutboxTrackingCode(outboxTrackingCode).orElse(null);
                if(Objects.isNull(flog)) {
                    LogOutboxServiceImpl.log.error("On saving new failed notification with outbox tracking code:{}, we couldn't find related record at time :{}",outboxTrackingCode,System.currentTimeMillis());
                    return null;
                }
            }
            flog.setStatus(OutboxEventStatus.UNSENT);
            flog.setCreatedDate(new Date());
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
    public void retry(FailedLog failedLog) {
        //send to external service again
    }

    public int changeStatusToSent(int outboxTrackingCode) {
        try {
            FailedLog failedLog = failedLogRepository.findByOutboxTrackingCode(outboxTrackingCode).orElse(null);
            if (Objects.nonNull(failedLog)) {
                return failedLogRepository.updateStatusInDistinctTransaction(failedLog.getId(), OutboxEventStatus.SENT, OutboxEventStatus.getAllValidStatusesForSent());
            }
            return 0;
        }catch (Exception e){
            log.error("On changing status of failed log with outbox tracking code:{}, exception occurred at time:{}, exception is:{}",outboxTrackingCode,System.currentTimeMillis(),e);
            return 0;
        }
    }
}
