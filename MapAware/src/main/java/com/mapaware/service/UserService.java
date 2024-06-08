package com.mapaware.service;

import com.mapaware.model.dto.EventDTO;
import com.mapaware.model.dto.UserDTO;
import com.mapaware.model.entity.EventEntity;
import com.mapaware.model.entity.UserEntity;
import com.mapaware.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

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

    public String saveUserProfileImage(MultipartFile image) throws Exception {

    try {
        String fileName = UUID.randomUUID().toString();
        byte[] bytes = image.getBytes();
        String fileOriginalName = image.getOriginalFilename();

        long fileSize = image.getSize();
        long maxFileSize = 5 * 1024 * 1024;

        if (fileSize > maxFileSize) {
            return "File size exceeds";
        }

        if (
            !fileOriginalName.endsWith(".jpg") &&
            !fileOriginalName.endsWith(".png") &&
            !fileOriginalName.endsWith(".jpeg")) {
            return "Invalid file format (JPG, PNG, JPEG ALLOWED)";
        }

        String fileExtension = fileOriginalName.substring(fileOriginalName.lastIndexOf("."));
        String newFileName = fileName + fileExtension;

        File folder = new File("src/main/resources/static/uploads");

        if(!folder.exists()) {
            folder.mkdirs();
        }

        Path filePath = Paths.get("src/main/resources/static/uploads/" + newFileName);
        Files.write(filePath, bytes);


        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userRepository.findByUsername(currentUsername).orElseThrow(() -> new RuntimeException("User not found"));
        currentUser.setProfileImage("/uploads/"+newFileName);
        userRepository.save(currentUser);




        return "File uploaded successfully";



    } catch (Exception e) {
        throw new Exception(e.getMessage());
    }

    }

    public String getCurrentUserProfileImage(){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userRepository.findByUsername(currentUsername).orElseThrow(() -> new RuntimeException("User not found"));
        return currentUser.getProfileImage();
    }

}
