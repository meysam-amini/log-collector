//package com.meysam.logcollector.addlog.integrationtest;
//
//import com.meysam.logcollector.addlog.controller.AddLogController;
//import com.meysam.logcollector.addlog.repository.LogRepository;
//import com.meysam.logcollector.addlog.service.api.DistinctLogService;
//import com.meysam.logcollector.addlog.service.api.LogService;
//import com.meysam.logcollector.common.model.dtos.dto.AddLogRequestDto;
//import com.meysam.logcollector.common.model.dtos.dto.LogDto;
//import com.meysam.logcollector.common.model.dtos.enums.LogType;
//import com.meysam.logcollector.common.model.dtos.enums.OutboxEventStatus;
//import com.meysam.logcollector.common.model.entities.entity.LogEntity;
//import com.meysam.logcollector.common.security.SecurityConfig;
//import com.meysam.logcollector.common.service.feign.api.ExternalServiceFeignClient;
//import io.restassured.RestAssured;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.ResponseEntity;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.Date;
//
//import static org.mockito.ArgumentMatchers.any;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class})
//@AutoConfigureMockMvc(addFilters = false)
//class AddLogControllerIT {
//
//    @MockBean
//    private ExternalServiceFeignClient externalService;
//    @Autowired
//    private DistinctLogService distinctLogService;
//    @MockBean
//    private  KafkaTemplate<String, LogDto> kafkaTemplate;
//    @Autowired
//    private LogRepository logRepository;
//
//    @Autowired
//    private LogService logService;
//
//    private AddLogController addLogController;
//
//    @BeforeAll
//    void init(){
//        ReflectionTestUtils.setField(logService, "NEW_LOG_TOPIC", "something");
//        ReflectionTestUtils.setField(logService, "NEW_SENT_LOG_ADDED", "something");
//
//        // we do this because we don't want to test against security and deal with keycloak and security stuff
//        addLogController = new AddLogController(logService);
//
//    }
//
//    @Test
//    public void test_sendLogToExternalService_externalServiceOkAndDB_OK_WeCanFindTheAddedLog() {
//
//        AddLogRequestDto requestDto = new AddLogRequestDto("test message", "service1", "requestId123", LogType.INFO);
//        LogEntity logEntity =  LogEntity.builder()
//                .requestId(requestDto.requestId())
//                .body(requestDto.body())
//                .serviceName(requestDto.serviceName())
//                .type(requestDto.type())
//                .build();
//
//        LogEntity savedLogEntity =  LogEntity.builder()
//                .requestId(requestDto.requestId())
//                .body(requestDto.body())
//                .serviceName(requestDto.serviceName())
//                .type(requestDto.type())
//                .id(1L)
//                .processed(true)
//                .status(OutboxEventStatus.SENT)
//                .createdDate(new Date())
//                .retryCount(null)
//                .outboxTrackingCode(null)
//                .build();
//
//        //kafkaTemplate.send() method would be ok
//        //Mockito.when(kafkaTemplate.send(any(),any())).thenThrow(new RuntimeException());
//
//        //external service doesn't respond
//        Mockito.when(externalService.sendLogToExternalApi(any())).thenReturn(ResponseEntity.ok("ok"));
//
////        Mockito.when(logRepository.save(logEntity)).thenReturn(savedLogEntity);
////
////        Mockito.when(distinctLogService.addLogToDbInDistinctTransaction(requestDto,OutboxEventStatus.SENT)).thenReturn(savedLogEntity);
//
//        Assertions.assertNotNull(addLogController.sendLogToExternalService(requestDto).getBody());
//    }
//
//    @Test
//    public void testAddLogToQueue_Success() throws Exception {
//    }
//
//    @Test
//    public void testAddLog(){
//
//    }
//
//}