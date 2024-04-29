package com.meysam.logcollector.querylogs.service.impl;

import com.meysam.logcollector.common.elastic.model.IndexedLog;
import com.meysam.logcollector.querylogs.repository.LogSearchRepository;
import com.meysam.logcollector.querylogs.service.api.LogQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogQueryServiceImpl implements LogQueryService {

    private final LogSearchRepository logSearchRepository;

    @Override
    public Page<IndexedLog> findAllByText(String txt, int pageNo, int pageSize) {
        return logSearchRepository.findAllByBodyContains(txt,PageRequest.of(pageNo,pageSize));
    }

    @Override
    public Page<IndexedLog> findAll(int pageNo, int pageSize) {
        return logSearchRepository.findAll(PageRequest.of(pageNo,pageSize));
    }
}
