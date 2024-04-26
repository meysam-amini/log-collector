package com.meysam.logcollector.addlog.controller;

import com.meysam.logcollector.addlog.service.api.LogService;
import com.meysam.logcollector.common.model.dtos.dto.AddLogRequestDto;
import com.meysam.logcollector.common.model.dtos.dto.AddLogResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class AddLogController {

    private final LogService logService;

    @PostMapping("queue")
    @PreAuthorize("hasAnyAuthority('ROLE_USER_LEVEL_1')")
    public ResponseEntity<AddLogResponseDto> addLogToQueue(@RequestBody AddLogRequestDto addLogRequestDto){
        return ResponseEntity.ok(logService.addLogToQueue(addLogRequestDto));
    }

    @PostMapping("direct")
    @PreAuthorize("hasAnyAuthority('ROLE_USER_LEVEL_1')")
    public ResponseEntity<AddLogResponseDto> sendLogToExternalService(@RequestBody AddLogRequestDto addLogRequestDto/*, JwtAuthenticationToken token*/){
        return logService.sendLogToExternalService(addLogRequestDto/*+token.getToken().getTokenValue()*/);
    }
}
