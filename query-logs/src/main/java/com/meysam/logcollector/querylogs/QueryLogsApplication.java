package com.meysam.logcollector.querylogs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.meysam.logcollector.*.*.*"})
@EnableJpaRepositories(basePackages = "com.meysam.logcollector.*.*.*")
@EntityScan(basePackages = "com.meysam.logcollector.*.*.*")
@EnableFeignClients(basePackages = {"com.meysam.logcollector.common.service.feinclients.*"})
public class QueryLogsApplication {

    public static void main(String []args){
        SpringApplication.run(QueryLogsApplication.class);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource= new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
