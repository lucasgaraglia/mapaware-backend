package com.mapaware.service;

import com.mapaware.Jwt.JwtService;
import com.mapaware.model.dto.AuthResponse;
import com.mapaware.model.dto.LoginRequest;
import com.mapaware.model.dto.RegisterRequest;
import com.mapaware.model.entity.RoleEnum;
import com.mapaware.model.entity.UserEntity;
import com.mapaware.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final IUserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            UserDetails user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            String token = jwtService.generateToken(user);
            return AuthResponse.builder()
                    .token(token)
                    .build();
        } catch (BadCredentialsException e) {
            return AuthResponse.builder()
                    .token(null)
                    .build();
        } catch (UsernameNotFoundException e) {
            return AuthResponse.builder()
                    .token(null)
                    .build();
        }
    }

    public AuthResponse register(RegisterRequest request) {
        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(RoleEnum.USER)
                .lastname(request.getLastname())
                .name(request.getName())
                .birthdate(request.getBirthdate())
                .profileImage(null)
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .token(jwtService.generateToken(user))
                .build();
    }
}
