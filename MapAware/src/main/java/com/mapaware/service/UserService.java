package com.mapaware.service;

import com.mapaware.model.dto.EventDTO;
import com.mapaware.model.dto.ProfileImageUploadRequest;
import com.mapaware.model.dto.UserDTO;
import com.mapaware.model.entity.EventEntity;
import com.mapaware.model.entity.UserEntity;
import com.mapaware.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final EventService eventService;

    public UserEntity loadUserEntityByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public UserDTO getCurrentUserDetails() {

        eventService.refreshEvents();
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
                .profileImage(currentUser.getProfileImage())
                .build();
    }


    public UserDTO getUserDetailsByUsername(String username) {
        eventService.refreshEvents();
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
                .profileImage(currentUser.getProfileImage())
                .build();
    }

    public boolean validateUser(String username, String role) {

        UserDetails user = userDetailsService.loadUserByUsername(username);
        System.out.println(user.getAuthorities().contains(new SimpleGrantedAuthority(role)));

        if (user.getAuthorities().contains(new SimpleGrantedAuthority(role))) {
            return true;
        }
        return false;

    }

    public void saveUserProfileImage(ProfileImageUploadRequest profileImage) throws IOException {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userRepository.findByUsername(currentUsername).orElseThrow(() -> new RuntimeException("User not found"));
        currentUser.setProfileImage(profileImage.getPath());
        userRepository.save(currentUser);
    }

}
