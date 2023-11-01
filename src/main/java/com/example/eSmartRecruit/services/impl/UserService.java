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

//    public User getUserByUsernameAndEmail(String username, String email) {
//        Optional<User> userOptional = userRepository.findByUsernameAndEmail(username, email);
//        return userOptional.orElse(null);
//    }
    public  String updateUserpassword(String username,String newpassword){
        User exUser = userRepository.findByUsername(username).orElse(null);
        if( exUser == null){
            return "No user";
        }
        exUser.setPassword(newpassword);
        try{
            userRepository.save(exUser);
            return "Success";
        }catch(Exception e){
            return "Error";
        }

    }

    public boolean isEnabled(Integer id){
        return userRepository.findById(id).get().isEnabled();
    }

    public User getUserById(Integer id){
        return userRepository.findById(id).orElse(null);
    }

    public  User updateUser(User user ,Integer id){
        User exUser = userRepository.findById(id).orElse(null);
        if( exUser == null){

        }
        exUser.setEmail(user.getEmail());
        exUser.setPhoneNumber(user.getPhoneNumber());
        try{
            userRepository.save(exUser);
        }catch(Exception e){
            return null;
        }
        return exUser;
    }
}
