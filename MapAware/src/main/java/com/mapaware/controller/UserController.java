package com.mapaware.controller;

import com.mapaware.auth.AuthResponse;
import com.mapaware.auth.LoginRequest;
import com.mapaware.persistence.entity.EventDTO;
import com.mapaware.persistence.entity.EventEntity;
import com.mapaware.persistence.entity.UserDTO;
import com.mapaware.persistence.entity.UserEntity;
import com.mapaware.persistence.repository.IUserRepository;
import com.mapaware.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
//@PreAuthorize("permitAll()")
public class UserController {

    private final IUserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<UserDTO> getUserDetails(){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("userCONTROLLER USER: "+currentUsername);
        UserEntity currentUser = userRepository.findByUsername(currentUsername).orElseThrow(() -> new UsernameNotFoundException("User not found."));

//        -------
        Collection<EventDTO> eventDTOList = new ArrayList<EventDTO>();

        for (EventEntity event : currentUser.getEvents()) {
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


        UserDTO userDetails= UserDTO.builder()
                .username(currentUser.getUsername())
                .email(currentUser.getEmail())
                .name(currentUser.getName())
                .lastname(currentUser.getLastname())
                .birthdate(currentUser.getBirthdate())
                .role(currentUser.getRole())
                .events(eventDTOList)
                .build();
        return ResponseEntity.ok(userDetails);
    }

}