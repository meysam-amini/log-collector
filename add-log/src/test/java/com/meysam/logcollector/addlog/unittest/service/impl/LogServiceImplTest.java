package com.meysam.logcollector.addlog.unittest.service.impl;

import com.meysam.logcollector.addlog.repository.LogRepository;
import com.meysam.logcollector.addlog.service.api.DistinctLogService;
import com.meysam.logcollector.addlog.service.api.LogService;
import com.meysam.logcollector.addlog.service.impl.LogServiceImpl;
import com.meysam.logcollector.common.exception.exceptions.BusinessException;
import com.meysam.logcollector.common.exception.exceptions.DataBaseException;
import com.meysam.logcollector.common.exception.exceptions.ServicesException;
import com.meysam.logcollector.common.exception.messages.LocaleMessageSourceService;
import com.meysam.logcollector.common.model.dtos.dto.AddLogRequestDto;
import com.meysam.logcollector.common.model.dtos.dto.AddLogResponseDto;
import com.meysam.logcollector.common.model.dtos.dto.LogDto;
import com.meysam.logcollector.common.model.dtos.enums.LogType;
import com.meysam.logcollector.common.model.dtos.enums.OutboxEventStatus;
import com.meysam.logcollector.common.model.entities.entity.LogEntity;
import com.meysam.logcollector.common.service.feign.api.ExternalServiceFeignClient;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LogServiceImplTest {

    @Mock
    private ExternalServiceFeignClient externalService;
    @Mock
    private DistinctLogService distinctLogService;
    @Mock
    private KafkaTemplate<String, LogDto> kafkaTemplate;
    @Mock
    private LogRepository logRepository;

    private LogService logService;

    @BeforeAll
    void init() {
        MockitoAnnotations.openMocks(this);
        logService = new LogServiceImpl(externalService, distinctLogService, logRepository, kafkaTemplate);
        ReflectionTestUtils.setField(logService, "NEW_LOG_TOPIC", "something");
        ReflectionTestUtils.setField(logService, "NEW_SENT_LOG_ADDED", "something");
    }


    @Test
    public void testAddLogToQueue_Given_KafkaIsOk_WeWillGet200() {
        AddLogRequestDto requestDto = new AddLogRequestDto("test message", "service1", "requestId123", LogType.INFO);

        // kafkaTemplate.send() method would be ok
        Mockito.when(kafkaTemplate.send(anyString(), any(LogDto.class))).thenReturn(new CompletableFuture<>());

        AddLogResponseDto responseDto = logService.addLogToQueue(requestDto);
        Assertions.assertTrue(responseDto.processed());
    }

    @Test
    public void testAddLogToQueue_Given_KafkaHasExceptionAndExternalServiceIsOkAndWeCouldSaveLog_WeWillGet200() {
        AddLogRequestDto requestDto = new AddLogRequestDto("test message", "service1", "requestId123", LogType.INFO);
        //kafka has problem
        Mockito.when(kafkaTemplate.send(anyString(), any(LogDto.class))).thenThrow(new RuntimeException());
        //external service ok
        Mockito.when(externalService.sendLogToExternalApi(any())).thenReturn(ResponseEntity.ok("ok"));
        //we could save log after sending to external services
        Mockito.when(distinctLogService.addLogToDbInDistinctTransaction(any(AddLogRequestDto.class), any(OutboxEventStatus.class)))
                .thenReturn(LogEntity.builder().id(1L).build());

        AddLogResponseDto responseDto = logService.addLogToQueue(requestDto);
        Assertions.assertTrue(responseDto.processed());
    }


    @Test
    public void testAddLogToQueue_Given_KafkaHasExceptionAndExternalServiceIsOkAndWeCouldNotSaveLog_WeWillGet200() {
        AddLogRequestDto requestDto = new AddLogRequestDto("test message", "service1", "requestId123", LogType.INFO);
        //kafka has problem
        Mockito.when(kafkaTemplate.send(anyString(), any(LogDto.class))).thenThrow(new RuntimeException());
        //external service ok
        Mockito.when(externalService.sendLogToExternalApi(any())).thenReturn(ResponseEntity.ok("ok"));
        //we could not save log after sending to external services(int that case we rise DataBaseException)
        Mockito.when(distinctLogService.addLogToDbInDistinctTransaction(any(AddLogRequestDto.class), any(OutboxEventStatus.class)))
                .thenThrow(new DataBaseException("db problem"));

        AddLogResponseDto responseDto = logService.addLogToQueue(requestDto);
        Assertions.assertTrue(responseDto.processed());
    }


    @Test
    public void testAddLogToQueue_Given_KafkaHasExceptionAndExternalServiceHasProblem_WeWillHaveServiceException() {

        AddLogRequestDto requestDto = new AddLogRequestDto("test message", "service1", "requestId123", LogType.INFO);

        //kafka has problem
        Mockito.when(kafkaTemplate.send(anyString(), any(LogDto.class))).thenThrow(new RuntimeException());
        //external service doesn't respond, and after that, we will have ServicesException
        Mockito.when(externalService.sendLogToExternalApi(any())).thenThrow(new RuntimeException());

        Assertions.assertThrows(ServicesException.class, () -> logService.addLogToQueue(requestDto));
    }


}