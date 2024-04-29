package com.meysam.logcollector.addlog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meysam.logcollector.common.model.dtos.dto.LogDto;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class KafkaSerializerConfig implements Serializer<LogDto> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, LogDto data) {
        try {
            if (data == null) {
                System.out.println("Null received at serializing");
                return null;
            }
            System.out.println("Serializing...");
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing LogDto to byte[]");
        }
    }

    @Override
    public void close() {
    }

}