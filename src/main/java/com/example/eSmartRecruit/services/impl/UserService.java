package com.example.eSmartRecruit.services.impl;

import com.example.eSmartRecruit.dto.EditUserDto;
import com.example.eSmartRecruit.exception.NotFoundException;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.repositories.UserRepos;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepos userRepository;

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public boolean isEnabled(Integer id){
        return userRepository.findById(Long.valueOf(id)).get().isEnabled();
    }

    public User getUserDetail(Long id ) {
        try {
            final Optional<User> user = userRepository.findById(id);
            if(user.isEmpty()){
                throw new NotFoundException("Can not found user");
            }
            return user.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean editUserInformation(Integer id , EditUserDto editUserDto){
        try{
            if(!userRepository.existsById(Long.valueOf(id))){
                throw new NotFoundException("Can't found user");
            }
            editUserDto.setId(Optional.of(id));
            var editUser = userRepository.save(
                    User.builder()
                            .id(id)
                            .username(editUserDto.getUsername())
                            .email(editUserDto.getEmail())
                            .phoneNumber(editUserDto.getPhoneNumber())
                            .updateDate((Date) Calendar.getInstance().getTime())
                            .build()
            );
            return true;
        } catch (Exception e){
            throw  new RuntimeException(e);
        }
    }
}
