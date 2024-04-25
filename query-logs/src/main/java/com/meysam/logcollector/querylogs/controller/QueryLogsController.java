package com.meysam.logcollector.querylogs.controller;

import com.meysam.logcollector.common.elastic.model.IndexedLog;
import com.meysam.logcollector.querylogs.model.QueryLogsDto;
import com.meysam.logcollector.querylogs.service.api.LogQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("guery-logs")
@RequiredArgsConstructor
public class QueryLogsController {

    private final LogQueryService logQueryService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER_LEVEL_1')")
    public ResponseEntity<Page<IndexedLog>> queryLogs(@RequestBody QueryLogsDto queryLogsDto){
        return ResponseEntity.ok(logQueryService.findAllByText(queryLogsDto.getTxt(), queryLogsDto.getPageNo(), queryLogsDto.getPageSize()));
    }
}
