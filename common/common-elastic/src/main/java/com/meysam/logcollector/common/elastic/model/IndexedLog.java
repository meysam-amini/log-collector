package com.meysam.logcollector.common.elastic.model;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data@Builder
@Document(indexName = "logs", createIndex = true)
public class IndexedLog {

    @Id
    private Long id;
    @Field(type = FieldType.Text, name = "body")
    private String body;
    @Field(type = FieldType.Text, name = "serviceName")
    private String serviceName;
    @Field(type = FieldType.Text, name = "requestId")
    private String requestId;
    @Field(type = FieldType.Text, name = "type")
    private String type;
    @Field(type = FieldType.Boolean, name = "type")
    private boolean processed;

}