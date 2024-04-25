package com.meysam.logcollector.externalservices.model;


public record AddLogRequestDto(String body,
                               String serviceName,
                               String requestId,
                               LogType type) {
}
