package com.meysam.logcollector.outboxengine.job;

import com.meysam.logcollector.common.outbox.service.api.OutboxService;
import com.meysam.logcollector.outboxengine.service.api.LogOutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogsOutboxScheduler {

    private final LogOutboxService logOutboxService;

//    @Scheduled(cron = "${outbox.engin.delay}" , zone = "Asia/Tehran")
    @Scheduled(fixedDelay = 5000)
    public void scheduler(){

        log.info("start scheduling unsent logs job at time:{}",System.currentTimeMillis());
        long startTime= System.currentTimeMillis();
        try {
            logOutboxService.process();

        }catch (Exception e){
            log.error("Exception at processing unsent logs job at time:{}, exception is:{} ",System.currentTimeMillis(),e);
        }
        long endTime= System.currentTimeMillis();
        log.info("end of scheduling unsent logs job at time:{}, whole job took:{} minutes",System.currentTimeMillis(),(endTime-startTime)/60000);

    }
}
