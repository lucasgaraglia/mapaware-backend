package com.mapaware.service;

import com.mapaware.persistence.entity.EventEntity;
import com.mapaware.persistence.repository.IEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventService {

    private final IEventRepository eventRepository;

    public void postEvent(EventEntity event){

        eventRepository.save(event);

    }

    public Collection<EventEntity> getEvents(){
        return eventRepository.findAll();
    }

    public void deleteEventById(Long id){
        eventRepository.deleteById(id);
    }
}
