package com.mapaware.persistence.repository;

import com.mapaware.persistence.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IEventRepository extends JpaRepository<EventEntity, Long> {
    Optional<EventEntity> findEventEntityById(long id);
}
