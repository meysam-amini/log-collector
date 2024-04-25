package com.meysam.logcollector.querylogs.service.api;

import com.meysam.logcollector.common.elastic.model.IndexedLog;
import org.springframework.data.domain.Page;

public interface LogQueryService {

    Page<IndexedLog> findAllByText(String txt,int pageNo,int pageSize);
}
