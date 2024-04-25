package com.meysam.logcollector.outboxengine.model;

import com.meysam.logcollector.common.model.enums.LogStatus;
import com.meysam.logcollector.common.model.enums.LogType;
import com.meysam.logcollector.common.outbox.model.entity.OutBox;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Table
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FailedLog extends OutBox {

    private String body;
    private String serviceName;
    private String requestId;
    private LogType type;
    private boolean processed;
    private LogStatus status;
}
