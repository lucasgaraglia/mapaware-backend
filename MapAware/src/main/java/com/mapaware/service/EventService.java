package com.mapaware.service;

import com.mapaware.persistence.entity.EventEntity;
import com.mapaware.persistence.repository.IEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final IEventRepository eventRepository;

    public void postEvent(EventEntity event){

        eventRepository.save(event);

    }
}
