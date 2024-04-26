package com.meysam.logcollector.outboxengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@ComponentScan(basePackages = {"com.meysam.logcollector.*.*.*"})
@EnableFeignClients(basePackages = {"com.meysam.logcollector.common.*.*.*"})
@EntityScan(basePackages = "com.meysam.logcollector.common.model.entities.entity")
@EnableJpaRepositories(basePackages={"com.meysam.logcollector.outboxengine.repository","com.meysam.logcollector.common.dao.repository"})
public class OutboxEngineApplication {

    public static void main(String []args){
        SpringApplication.run(OutboxEngineApplication.class);
    }


}