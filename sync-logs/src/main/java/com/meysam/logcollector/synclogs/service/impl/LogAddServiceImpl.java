package com.meysam.logcollector.synclogs.service.impl;

import com.meysam.logcollector.common.elastic.model.IndexedLog;
import com.meysam.logcollector.synclogs.repository.LogSaveRepository;
import com.meysam.logcollector.synclogs.service.api.LogAddService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogAddServiceImpl implements LogAddService {

    private final LogSaveRepository logSaveRepository;


    @Override
    public IndexedLog add(IndexedLog indexedLog) {
        return logSaveRepository.save(indexedLog);
    }

    @Override
    public void addAll(List<IndexedLog> indexedLogs) {
        logSaveRepository.saveAll(indexedLogs);
    }
}
