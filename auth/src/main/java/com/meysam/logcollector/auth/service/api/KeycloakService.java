package com.meysam.logcollector.auth.service.api;

import com.meysam.logcollector.auth.model.enums.MemberLevel;
import com.meysam.logcollector.common.model.dtos.dto.LoginRequestDto;
import com.meysam.logcollector.common.model.dtos.dto.LoginResponseDto;
import com.meysam.logcollector.common.model.dtos.dto.RegisterUserRequestDto;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Member;
import java.util.List;

public interface KeycloakService {


    ResponseEntity registerUser(RegisterUserRequestDto registerDto, MemberLevel memberLevel);

    LoginResponseDto loginUser(LoginRequestDto loginDto);

    ResponseEntity getTokenByRefreshToken(LoginRequestDto loginDto);

}
