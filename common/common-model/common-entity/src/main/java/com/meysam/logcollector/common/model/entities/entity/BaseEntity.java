package com.meysam.logcollector.common.model.entities.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private long id;

    @Version
    @Column(nullable = false)
    private Long version= 0L;

    @Column(nullable = false)
    private boolean enabled=true;

    @CreationTimestamp
    private Date createdDate = new Date();

    @UpdateTimestamp
    private Date updatedDate = new Date();

}
