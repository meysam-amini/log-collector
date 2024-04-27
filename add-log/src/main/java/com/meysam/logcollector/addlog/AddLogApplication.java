package com.meysam.logcollector.addlog;

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
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.meysam.logcollector.*.*"
        ,"com.meysam.logcollector.*.*.*"})
@EnableJpaRepositories(basePackages = "com.meysam.logcollector.*.*")
@EntityScan(basePackages = "com.meysam.logcollector.*.*")
@EnableFeignClients(basePackages = {"com.meysam.logcollector.common.*.*.*"})
@EnableMongoRepositories(basePackages = "com.meysam.logcollector.addlog.repository")
@EnableRetry
public class AddLogApplication {

    public static void main(String []args){
        SpringApplication.run(AddLogApplication.class);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource= new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
