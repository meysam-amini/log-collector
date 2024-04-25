package com.meysam.logcollector.common.model.entity;

import com.meysam.logcollector.common.model.enums.LogStatus;
import com.meysam.logcollector.common.model.enums.LogType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity@Table@Builder
public class LogEntity extends BaseEntity{

    private String body;
    private String serviceName;
    private String requestId;
    private LogType type;
    private boolean processed;
    private LogStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogEntity)) return false;
        LogEntity logEntity = (LogEntity) o;
        return getServiceName().equals(logEntity.getServiceName()) && getRequestId().equals(logEntity.getRequestId()) && getType() == logEntity.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServiceName(), getRequestId(), getType());
    }
}
