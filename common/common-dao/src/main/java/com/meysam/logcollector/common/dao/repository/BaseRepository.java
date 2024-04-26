package com.meysam.logcollector.common.dao.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<ENTITY_TYPE> extends JpaRepository<ENTITY_TYPE, Long>, CrudRepository<ENTITY_TYPE, Long> {
}
