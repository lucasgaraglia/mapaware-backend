package com.mapaware.service;

import com.mapaware.model.dto.EventDTO;
import com.mapaware.model.dto.UserDTO;
import com.mapaware.model.entity.EventEntity;
import com.mapaware.model.entity.UserEntity;
import com.mapaware.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository userRepository;

    public UserEntity loadUserEntityByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public UserDTO getCurrentUserDetails() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println("userSERVICE USER: "+currentUsername);
        UserEntity currentUser = userRepository.findByUsername(currentUsername).orElseThrow(() -> new UsernameNotFoundException("User not found."));

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


        return UserDTO.builder()
                .username(currentUser.getUsername())
                .email(currentUser.getEmail())
                .name(currentUser.getName())
                .lastname(currentUser.getLastname())
                .birthdate(currentUser.getBirthdate())
                .role(currentUser.getRole())
                .events(eventDTOList)
                .build();
    }


    public UserDTO getUserDetailsByUsername(String username) {
        UserEntity currentUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found."));

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


        return UserDTO.builder()
                .username(currentUser.getUsername())
                .email(currentUser.getEmail())
                .name(currentUser.getName())
                .lastname(currentUser.getLastname())
                .birthdate(currentUser.getBirthdate())
                .role(currentUser.getRole())
                .events(eventDTOList)
                .build();
    }
}
