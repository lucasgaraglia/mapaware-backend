package com.mapaware.service;

import com.mapaware.model.dto.EventDTO;
import com.mapaware.model.entity.EventEntity;
import com.mapaware.model.entity.UserEntity;
import com.mapaware.repository.IEventRepository;
import com.mapaware.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final IEventRepository eventRepository;
    private final IUserRepository userRepository;
//    private final IEventRepositoryPageable eventRepositoryPageable;

    public void postEvent(EventEntity event){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        event.setUser(currentUser);
        event.setDateTime(LocalDateTime.now());
        eventRepository.save(event);
    }

    public Collection<EventEntity> getEvents(){
        refreshEvents();
        return eventRepository.findAll();
    }

    public void deleteEventById(Long id){
        eventRepository.deleteById(id);
    }

    public Collection<EventDTO> getEventsPagination(int pag, int cant){
        refreshEvents();

        Pageable pageable = PageRequest.of(pag, cant);
        Page<EventEntity> events = eventRepository.findAll(pageable);

        Collection<EventDTO> eventDTOList = new ArrayList<>();

        for (EventEntity event : events) {
            EventDTO eventDTO = EventDTO.builder()
                    .id(event.getId())
                    .dateTime(event.getDateTime())
                    .description(event.getDescription())
                    .category(event.getCategory())
                    .degree(event.getDegree())
                    .latitude(event.getLatitude())
                    .longitude(event.getLongitude())
                    .username(event.getUser().getUsername())
                    .build();
            eventDTOList.add(eventDTO);
        }
        return eventDTOList;
    }

    public void refreshEvents(){
        List<EventEntity> events = eventRepository.findAll();
        for (EventEntity event : events) {
            if(isMoreThanOneDayAgo(event.getDateTime())){
                eventRepository.deleteById(event.getId());
            }
        }
    }

    public static boolean isMoreThanOneDayAgo(LocalDateTime dateTime) {
        LocalDateTime oneDayAgo = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
        return dateTime.isBefore(oneDayAgo);
    }

    public Collection<EventEntity> getEventsFiltered(String category) {
        return eventRepository.findEventEntityByCategory(category);
    }
}
