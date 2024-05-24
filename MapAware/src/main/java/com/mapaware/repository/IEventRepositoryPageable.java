package com.mapaware.repository;

import com.mapaware.model.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

@Repository
public interface IEventRepositoryPageable extends PagingAndSortingRepository<EventEntity, Long> {
    Page<EventEntity> findAll(Pageable pageable);
}
