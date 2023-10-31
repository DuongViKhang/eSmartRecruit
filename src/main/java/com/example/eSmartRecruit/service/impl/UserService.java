package com.example.eSmartRecruit.service.impl;

import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.repositories.UserRepos;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepos userRepository;

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public User getUserByUsernameAndEmail(String username, String email) {
        Optional<User> userOptional = userRepository.findByUsernameAndEmail(username, email);
        return userOptional.orElse(null);
    }
}