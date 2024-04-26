package com.meysam.logcollector.auth.controller;

import com.meysam.logcollector.auth.model.enums.MemberLevel;
import com.meysam.logcollector.auth.service.api.KeycloakService;
import com.meysam.logcollector.common.model.dtos.dto.LoginRequestDto;
import com.meysam.logcollector.common.model.dtos.dto.LoginResponseDto;
import com.meysam.logcollector.common.model.dtos.dto.RegisterUserRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth-user")
public class UserAuthController {

    private final KeycloakService keycloakService;

    @PostMapping(value = "login",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto loginRequestDto){
        return keycloakService.loginUser(loginRequestDto);
    }

    @PostMapping("register")
    public ResponseEntity register(@RequestBody @Valid RegisterUserRequestDto registerRequestDto){
        return keycloakService.registerUser(registerRequestDto, MemberLevel.MEMBERS_LEVEL_1);

    }

    @PostMapping("refresh-token")
    public ResponseEntity getRefreshToken(@RequestBody @Valid LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(keycloakService.getTokenByRefreshToken(loginRequestDto));

    }

}
