package com.meysam.logcollector.addlog.repository;

import com.meysam.logcollector.common.model.entity.LogEntity;
import com.meysam.logcollector.common.model.enums.LogStatus;
import ir.pasargad.logcollector.common.dao.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LogRepository extends BaseRepository<LogEntity> {

    // TODO: 25.04.24 - its better to define a list of valid statuses for current status
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying
    @Query("update LogEntity l set l.status=:status where l.id=:id")
    int updateStatusInDistinctTransaction(long id, LogStatus status);
}
