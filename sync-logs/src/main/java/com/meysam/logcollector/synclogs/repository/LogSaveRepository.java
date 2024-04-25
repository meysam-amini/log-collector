package com.meysam.logcollector.synclogs.repository;

import com.meysam.logcollector.common.elastic.model.IndexedLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LogSaveRepository extends ElasticsearchRepository<IndexedLog, Long> {

}
