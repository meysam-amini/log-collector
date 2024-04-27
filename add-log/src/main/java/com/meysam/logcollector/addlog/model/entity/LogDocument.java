package com.meysam.logcollector.addlog.model.entity;

import com.meysam.logcollector.common.model.dtos.enums.LogType;
import com.meysam.logcollector.common.model.entities.entity.LogEntity;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Objects;

@Document(collation = "log")
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor@Builder
public class LogDocument {

    @MongoId(FieldType.OBJECT_ID)
    private Long id;

    private String body;
    private String serviceName;
    @Indexed
    private String requestId;
    private LogType type;
    private boolean processed;

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
