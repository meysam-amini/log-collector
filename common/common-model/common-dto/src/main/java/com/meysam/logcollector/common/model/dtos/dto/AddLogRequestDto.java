package com.meysam.logcollector.common.model.dtos.dto;


import com.meysam.logcollector.common.model.dtos.enums.LogType;

public record AddLogRequestDto(String body,
                               String serviceName,
                               String requestId,
                               LogType type) {
}
