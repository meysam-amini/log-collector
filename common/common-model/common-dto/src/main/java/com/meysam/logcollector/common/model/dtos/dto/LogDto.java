package com.meysam.logcollector.common.model.dtos.dto;

import com.meysam.logcollector.common.model.dtos.enums.LogType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter@Builder
public class LogDto {

    private Long id;
    private String body;
    private String serviceName;
    private String requestId;
    private LogType type;
    private boolean processed;
}
