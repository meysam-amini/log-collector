package com.meysam.logcollector.synclogs.consumer;

import com.meysam.logcollector.common.elastic.model.IndexedLog;
import com.meysam.logcollector.common.model.dto.LogDto;
import com.meysam.logcollector.synclogs.service.api.LogAddService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogsConsumer {


    private ExecutorService executor = new ThreadPoolExecutor(100, 200, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024*2), new ThreadPoolExecutor.AbortPolicy());

    private final LogAddService logAddService;

    @KafkaListener(topics ="${spring.kafka.topics.new-log-added}", containerFactory = "kafkaListenerContainerFactory")
    public void handleNewLogs(List<ConsumerRecord<String, LogDto>> records) {
        List<IndexedLog> indexedLogs = records.stream()
                .map(ConsumerRecord::value)
                .map(this::mapToIndexedLog)
                .toList();
        executor.submit(new LogsHandler(indexedLogs));
    }


    private class LogsHandler implements Runnable{

        private List<IndexedLog> indexedLogs;

        public LogsHandler(List<IndexedLog> indexedLogs){
            this.indexedLogs=indexedLogs;
        }
        @Override
        public void run() {

            logAddService.addAll(indexedLogs);

        }
    }


    private IndexedLog mapToIndexedLog(LogDto logDto){

        return IndexedLog.builder()
                .body(logDto.getBody())
                .processed(logDto.isProcessed())
                .requestId(logDto.getRequestId())
                .serviceName(logDto.getServiceName())
                .type(logDto.getType().name())
                .build();
    }
}
