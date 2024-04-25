package com.meysam.logcollector.addlog.service.api;

import com.meysam.logcollector.addlog.feign.CustomRetrier;
import com.meysam.logcollector.addlog.feign.FeignErrorDecoder;
import com.meysam.logcollector.common.model.dto.AddLogRequestDto;
import com.meysam.logcollector.addlog.service.impl.ExternalServiceFallBackFactory;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "external-ws",fallbackFactory = ExternalServiceFallBackFactory.class, configuration = {CustomRetrier.class, FeignErrorDecoder.class})
public interface ExternalServiceFeignClient {

    @PostMapping("/test")
    ResponseEntity<String> sendLogToExternalApi(/*@RequestHeader("Authorization") String token, */@RequestBody @Valid AddLogRequestDto addLogRequestDto);

}
