package com.mapaware.service;

import com.mapaware.model.dto.EventDTO;
import com.mapaware.model.entity.EventEntity;
import com.mapaware.model.entity.UserEntity;
import com.mapaware.repository.IEventRepository;
import com.mapaware.repository.IEventRepositoryPageable;
import com.mapaware.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class EventService {

    private final IEventRepository eventRepository;
    private final IUserRepository userRepository;
    private final IEventRepositoryPageable eventRepositoryPageable;

    public void postEvent(EventEntity event){

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println("CONTROLLER USER: "+currentUsername);
        UserEntity currentUser = userRepository.findByUsername(currentUsername).orElseThrow(() -> new UsernameNotFoundException("User not found."));
        event.setUser(currentUser);
        event.setDateTime(LocalDateTime.now());

        eventRepository.save(event);

    }

    public Collection<EventEntity> getEvents(){
        return eventRepository.findAll();
    }

    public void deleteEventById(Long id){
        eventRepository.deleteById(id);
    }

    public Collection<EventDTO> getEventsPagination(int pag, int cant){
        Pageable pageable = (Pageable) PageRequest.of(pag, cant);
        Page<EventEntity> events = eventRepositoryPageable.findAll(pageable);

        Collection<EventDTO> eventDTOList = new ArrayList<EventDTO>();

        for (EventEntity event : events) {
            EventDTO eventDTO = EventDTO.builder()
                    .id(event.getId())
                    .dateTime(event.getDateTime())
                    .description(event.getDescription())
                    .category(event.getCategory())
                    .degree(event.getDegree())
                    .latitude(event.getLatitude())
                    .longitude(event.getLongitude())
                    .build();
            eventDTOList.add(eventDTO);
        }
        return eventDTOList;
    }
}
