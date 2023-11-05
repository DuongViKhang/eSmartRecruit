package com.example.eSmartRecruit.services.impl;

import com.example.eSmartRecruit.controllers.request_reponse.request.UserRequest;
import com.example.eSmartRecruit.exception.UserException;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.repositories.UserRepos;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepos userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUser(){
        return userRepository.findAll();
    }
    public User findByUsername(String username) throws UserException {
        return userRepository.findByUsername(username).orElseThrow(()->new UserException("Username not found!"));
    }

//    public User getUserByUsernameAndEmail(String username, String email) {
//        Optional<User> userOptional = userRepository.findByUsernameAndEmail(username, email);
//        return userOptional.orElse(null);
//    }
    public  String updateUserpassword(String username,String newpassword) throws UserException {
        User exUser = userRepository.findByUsername(username).orElseThrow(()->new UserException("User not found!"));
        newpassword = passwordEncoder.encode(newpassword);
        exUser.setPassword(newpassword);
        try{
            userRepository.save(exUser);
            return "Successfully saved";
        }catch(Exception e){
            return "Could not save";
        }

    }

    public boolean isEnabled(Integer id) throws UserException {
        return getUserById(id).isEnabled();
    }

    public User getUserById(Integer id) throws UserException {
        return userRepository.findById(id).orElseThrow(()->new UserException("UserNotFound!"));
    }

    public  User updateUser(UserRequest user , Integer id) throws UserException {
        User exUser = getUserById(id);
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
