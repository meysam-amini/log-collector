package com.meysam.logcollector.addlog.service.impl;

import com.meysam.logcollector.addlog.service.api.ExternalServiceFeignClient;
import com.meysam.logcollector.common.exception.exceptions.ServicesException;
import com.meysam.logcollector.common.exception.messagesLoader.LocaleMessageSourceService;
import com.meysam.logcollector.common.model.dto.AddLogRequestDto;
import com.meysam.logcollector.common.model.dto.LogDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalServiceFallBackFactory implements FallbackFactory<ExternalServiceFeignClient> {

    private final LocaleMessageSourceService messageSourceService;

    @Override
    public ExternalServiceFeignClient create(Throwable cause) {
        int status = ((FeignException) cause).status();
        String message = cause.getLocalizedMessage();
        return new ExternalServiceClientFallBack(cause,message,status, messageSourceService);
    }

    @RequiredArgsConstructor
    public class ExternalServiceClientFallBack implements ExternalServiceFeignClient {

        private final Throwable cause;
        private final String message;
        private final int status;
        private final LocaleMessageSourceService messageSourceService;


        @Override
        public ResponseEntity<String> sendLogToExternalApi(AddLogRequestDto addLogRequestDto) {
            log.error(status+" error occurred when sendLogToExternalApi method called external-ws/test with log:"+addLogRequestDto.toString()+" at time :{}",System.currentTimeMillis());
            ResponseEntity response = returnProperResponse(cause,status);
            throw new ServicesException(response.getBody().toString(),response.getStatusCode());
        }


        private ResponseEntity<String> returnProperResponse(Throwable cause, int status){
            if (cause instanceof FeignException && status ==-1) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(messageSourceService.getMessage("EXTERNAL_WS_PROBLEM"));
            } else {
                return ResponseEntity.status(status).body(message);
            }
        }


    }

}