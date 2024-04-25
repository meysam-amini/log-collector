package com.meysam.logcollector.common.model.entity;

import com.meysam.logcollector.common.model.enums.OutboxEventStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@MappedSuperclass
@Setter
@AllArgsConstructor
@NoArgsConstructor@SuperBuilder
public class OutBoxableBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "STATUS")
    private OutboxEventStatus status= OutboxEventStatus.UNSENT;

    @Column(name = "CREATEDDATE")
    @CreationTimestamp
    private Date createdDate = new Date();

    @Column(name = "TRACKINGCODE")
    private Integer outboxTrackingCode;

    @Column(name = "RETRYCOUNT")
    private Integer retryCount;
}
