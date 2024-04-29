package com.meysam.logcollector.addlog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meysam.logcollector.common.model.dtos.dto.LogDto;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class KafkaDeserializerConfig implements Deserializer<LogDto> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public LogDto deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                System.out.println("Null received at deserializing");
                return null;
            }
            System.out.println("Deserializing...");
            return objectMapper.readValue(new String(data, "UTF-8"), LogDto.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to LogDto");
        }
    }

    @Override
    public void close() {
    }

}