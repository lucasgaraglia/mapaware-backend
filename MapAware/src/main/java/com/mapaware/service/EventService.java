package com.mapaware.service;

import com.mapaware.model.entity.EventEntity;
import com.mapaware.model.entity.UserEntity;
import com.mapaware.repository.IEventRepository;
import com.mapaware.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class EventService {

    private final IEventRepository eventRepository;
    private final IUserRepository userRepository;

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
}
