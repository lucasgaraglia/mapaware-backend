package com.mapaware.service;

import com.mapaware.persistence.entity.UserEntity;
import com.mapaware.persistence.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository userRepository;

    public UserEntity loadUserEntityByUsername(String username) {
        return userRepository.findUserEntityByUsername(username).orElseThrow();
    }


}
