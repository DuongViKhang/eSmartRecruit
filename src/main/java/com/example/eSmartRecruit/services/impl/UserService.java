package com.example.eSmartRecruit.services.impl;

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
    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public boolean isEnabled(Integer id){
        return userRepository.findById(id).get().isEnabled();
    }

    public User getUserById(Integer id){
        return userRepository.findById(id).orElse(null);
    }

}
