package com.meysam.logcollector.common.service.feign.api;

import com.meysam.logcollector.common.model.dtos.dto.AddLogRequestDto;
import com.meysam.logcollector.common.service.feign.components.CustomRetryer;
import com.meysam.logcollector.common.service.feign.components.FeignErrorDecoder;
import com.meysam.logcollector.common.service.feign.impl.ExternalServiceFallBackFactory;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "external-service",url = "http://localhost:8092",fallbackFactory = ExternalServiceFallBackFactory.class, configuration = {CustomRetryer.class, FeignErrorDecoder.class})
public interface ExternalServiceFeignClient {

    @PostMapping("/test")
    ResponseEntity<String> sendLogToExternalApi(/*@RequestHeader("Authorization") String token, */@RequestBody @Valid AddLogRequestDto addLogRequestDto);

}
