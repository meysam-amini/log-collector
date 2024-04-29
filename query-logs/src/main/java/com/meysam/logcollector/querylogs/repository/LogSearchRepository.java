package com.meysam.logcollector.querylogs.repository;

import com.meysam.logcollector.common.elastic.model.IndexedLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LogSearchRepository extends ElasticsearchRepository<IndexedLog, Long> {

    Page<IndexedLog> findAllByBodyContains(String txt1, Pageable pageable);


}
