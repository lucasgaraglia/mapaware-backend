package com.mapaware.controller;

import com.mapaware.model.dto.EventDTO;
import com.mapaware.model.entity.EventEntity;
import com.mapaware.model.dto.UserDTO;
import com.mapaware.model.entity.UserEntity;
import com.mapaware.repository.IUserRepository;
import com.mapaware.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
//@PreAuthorize("permitAll()")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'PRO')")
    public ResponseEntity<UserDTO> getCurrentUserDetails(){
        UserDTO userDetails = userService.getCurrentUserDetails();
        return ResponseEntity.ok(userDetails);
    }

    @GetMapping("/details/{username}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'PRO')")
    public ResponseEntity<UserDTO> getUserDetailsByUsername(@PathVariable String username){
        UserDTO userDetails = userService.getUserDetailsByUsername(username);
        return ResponseEntity.ok(userDetails);
    }

    @GetMapping("/validate")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'PRO')")
    public ResponseEntity<Object> validateUser(@RequestParam(name = "user") String username,
                                               @RequestParam(name = "role") String role) {
        boolean isValid = userService.validateUser(username, role.toUpperCase());
        return ResponseEntity.ok(isValid);

    }

    @PutMapping("/image")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'PRO')")
    public ResponseEntity<String> uploadProfileImage(@RequestBody String profileImage) throws IOException {

            userService.saveUserProfileImage(profileImage);
            return ResponseEntity.ok("File uploaded successfully");


    }


}
