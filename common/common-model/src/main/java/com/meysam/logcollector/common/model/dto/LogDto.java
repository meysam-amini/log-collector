package com.meysam.logcollector.common.model.dto;

import com.meysam.logcollector.common.model.enums.LogType;
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
