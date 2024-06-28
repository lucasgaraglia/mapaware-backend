package com.mapaware.controller;

import com.mapaware.model.dto.AuthResponse;
import com.mapaware.service.AuthService;
import com.mapaware.model.dto.LoginRequest;
import com.mapaware.model.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@PreAuthorize("permitAll()") // default: endpoints publicos
public class UserAuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            if (response.getToken() == null) {
                return ResponseEntity.badRequest().body("Invalid username or password.");
            }
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body("Invalid username or password.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error procesando la request.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

}