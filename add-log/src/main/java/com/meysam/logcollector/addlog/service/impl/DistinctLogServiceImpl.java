package com.meysam.logcollector.addlog.service.impl;

import com.meysam.logcollector.addlog.repository.LogRepository;
import com.meysam.logcollector.addlog.service.api.DistinctLogService;
import com.meysam.logcollector.common.exception.exceptions.BusinessException;
import com.meysam.logcollector.common.exception.exceptions.DataBaseException;
import com.meysam.logcollector.common.model.dtos.dto.AddLogRequestDto;
import com.meysam.logcollector.common.model.dtos.enums.OutboxEventStatus;
import com.meysam.logcollector.common.model.entities.entity.LogEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistinctLogServiceImpl implements DistinctLogService {

    private final LogRepository logRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public LogEntity addLogToDbInDistinctTransaction(AddLogRequestDto addLogRequestDto, OutboxEventStatus status) {
        try {
            LogEntity logEntity = LogEntity.builder()
                    .body(addLogRequestDto.body())
                    .serviceName(addLogRequestDto.serviceName())
                    .requestId(addLogRequestDto.requestId())
                    .processed(true)
                    .type(addLogRequestDto.type())
                    .status(status)
                    .build();
            if(status.equals(OutboxEventStatus.UNSENT)){
                logEntity.setOutboxTrackingCode((UUID.randomUUID() + "" + System.currentTimeMillis()).hashCode());
                logEntity.setRetryCount(0);
            }
            logRepository.save(logEntity);
            return logEntity;
        }catch (Exception e){
            log.error("We couldn't persist the queuing failed logs on db! time:{} , log:{}, exception:{}",System.currentTimeMillis(),addLogRequestDto.toString(),e);
            throw new DataBaseException("INTERNAL_SERVER_ERROR");
        }
    }

    @Override
    public int updateLogStatusInDistinctTransaction(long id, OutboxEventStatus status) {
        return logRepository.updateStatusInDistinctTransaction(id,status);
    }
}
