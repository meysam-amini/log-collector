package com.meysam.logcollector.common.service.feign.components;

import feign.RetryableException;
import feign.Retryer;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@NoArgsConstructor
public class CustomRetryer implements Retryer {

    private int retryMaxAttempt=5;

    private long retryInterval=400;

    private int attempt = 1;


    public CustomRetryer(int retryMaxAttempt, Long retryInterval) {
        this.retryMaxAttempt = retryMaxAttempt;
        this.retryInterval = retryInterval;
    }

    @Override
    public void continueOrPropagate(RetryableException e) {
        log.info("Feign retry attempt {} due to {} ", attempt, e.getMessage());

        if(attempt++ == retryMaxAttempt){
            throw e;
        }
        try {
            Thread.sleep(retryInterval);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }

    }

    @Override
    public Retryer clone() {
        return new CustomRetryer(5, 400L);
    }
}
