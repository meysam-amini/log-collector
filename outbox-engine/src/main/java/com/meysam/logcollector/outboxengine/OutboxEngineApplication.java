package com.meysam.logcollector.outboxengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableFeignClients(basePackages = {"com.meysam.logcollector.common.*.*.*"})
public class OutboxEngineApplication {

    public static void main(String []args){
        SpringApplication.run(OutboxEngineApplication.class);
    }


}