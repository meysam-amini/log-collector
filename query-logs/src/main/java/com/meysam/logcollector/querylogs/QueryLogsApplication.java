package com.meysam.logcollector.querylogs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.meysam.logcollector.*.*.*"})
@EntityScan(basePackages = "com.meysam.logcollector.*.*.*")
@EnableElasticsearchRepositories(basePackages ={"com.meysam.logcollector.querylogs.repository"} )
public class QueryLogsApplication {

    public static void main(String []args){
        SpringApplication.run(QueryLogsApplication.class);
    }


}
