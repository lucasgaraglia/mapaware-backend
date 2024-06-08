package com.mapaware.controller;

import com.mapaware.model.dto.UserDTO;
import com.mapaware.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public ResponseEntity<String> uploadProfileImage(@RequestParam("image") MultipartFile image) throws Exception {
        
            return ResponseEntity.ok(userService.saveUserProfileImage(image));


    }

    @GetMapping("/get-image")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'PRO')")
    public ResponseEntity<String> getProfileImage(){
        return ResponseEntity.ok(userService.getCurrentUserProfileImage());
    }



}
