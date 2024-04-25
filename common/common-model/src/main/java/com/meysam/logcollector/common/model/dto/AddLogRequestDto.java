package com.meysam.logcollector.common.model.dto;

import com.meysam.logcollector.common.model.enums.LogType;

public record AddLogRequestDto(String body,
                               String serviceName,
                               String requestId,
                               LogType type) {
}
