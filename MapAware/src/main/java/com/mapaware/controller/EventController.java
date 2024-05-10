package com.mapaware.controller;

import com.mapaware.auth.AuthResponse;
import com.mapaware.auth.LoginRequest;
import com.mapaware.persistence.entity.EventEntity;
import com.mapaware.persistence.entity.UserEntity;
import com.mapaware.persistence.repository.IUserRepository;
import com.mapaware.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
//@PreAuthorize("permitAll()")
public class EventController {

    private final EventService eventService;
    private final IUserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<EventEntity> postEvent(@RequestBody EventEntity event){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userRepository.findUserEntityByUsername(currentUsername).orElseThrow(() -> new UsernameNotFoundException("User not found."));
        event.setUser(currentUser);
        eventService.postEvent(event);
        return ResponseEntity.ok(event);
    }
}
