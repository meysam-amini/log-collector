package com.meysam.logcollector.synclogs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.meysam.logcollector.common.model.dtos.*"
        ,"com.meysam.logcollector.common.elastic.*"})
@EnableElasticsearchRepositories(basePackages ={"com.meysam.logcollector.synclogs.repository"} )
public class SyncLogsApplication {

    public static void main(String []args){
        SpringApplication.run(SyncLogsApplication.class);
    }


}