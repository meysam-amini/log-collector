package com.meysam.logcollector.common.config.web;


import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate simpleRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    @LoadBalanced
    @Primary
    public RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }

}
