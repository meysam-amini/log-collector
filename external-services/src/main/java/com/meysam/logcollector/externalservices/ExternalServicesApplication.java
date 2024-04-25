package com.meysam.logcollector.externalservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.meysam.logcollector.*.*")
public class ExternalServicesApplication {

    public static void main(String []args){
        SpringApplication.run(ExternalServicesApplication.class);
    }


}
