package com.mapaware.repository;

import com.mapaware.model.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IEventRepository extends JpaRepository<EventEntity, Long> {
    Optional<EventEntity> findEventEntityById(Long id);

    void deleteById(Long id);
}
