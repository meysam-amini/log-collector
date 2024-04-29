package com.meysam.logcollector.addlog.consumer;

import com.meysam.logcollector.addlog.service.api.LogService;
import com.meysam.logcollector.common.model.dtos.dto.LogDto;
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

    private final LogService logService;

    @KafkaListener(topics ="${spring.kafka.topics.new-log}", containerFactory = "kafkaListenerContainerFactory")
    public void handleNewLogs(List<ConsumerRecord<String, LogDto>> records) {
        executor.submit(new LogsHandler(records));
    }


    private class LogsHandler implements Runnable{

        private List<ConsumerRecord<String, LogDto>> records;

        public LogsHandler(List<ConsumerRecord<String, LogDto>> records){
            this.records=records;
        }
        @Override
        public void run() {

            List<LogDto> logDtos = records.stream()
                    .map(ConsumerRecord::value).toList();

            logDtos.parallelStream().forEach(logService::sendLogsFromKafkaConsumerToExternalService);
        }
    }
}
