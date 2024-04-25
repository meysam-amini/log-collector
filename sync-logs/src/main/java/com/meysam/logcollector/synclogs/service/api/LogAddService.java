package com.meysam.logcollector.synclogs.service.api;

import com.meysam.logcollector.common.elastic.model.IndexedLog;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LogAddService {

    IndexedLog add (IndexedLog indexedLog);
    void addAll (List<IndexedLog> indexedLogs);
}
