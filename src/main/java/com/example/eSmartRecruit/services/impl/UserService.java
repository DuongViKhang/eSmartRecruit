package com.example.eSmartRecruit.services.impl;

import com.example.eSmartRecruit.authentication.request_reponse.RegisterRequest;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.controllers.request_reponse.request.UserRequest;
import com.example.eSmartRecruit.dto.EditUserDto;
import com.example.eSmartRecruit.dto.EditUserResponse;
import com.example.eSmartRecruit.exception.NotFoundException;
import com.example.eSmartRecruit.exception.UserException;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.models.enumModel.Role;
import com.example.eSmartRecruit.models.enumModel.UserStatus;
import com.example.eSmartRecruit.repositories.UserRepos;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepos userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUser() throws UserException {
        return userRepository.findAll();
    }
    public User findByUsername(String username) throws UserException {
        return userRepository.findByUsername(username).orElseThrow(()->new UserException("Username not found!"));
    }
    public String getUserRole(Integer id){
        return userRepository.findById(id).get().getRoleName().toString().trim();
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

    public String checkDuplicateEmail(User user){
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            return "This email is already used by another user!";
        }
        return null;
    }
    public String checkDuplicatePhone(User user){
        if(userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()){
            return "This phone number is already used by another user!";
        }
        return null;
    }

    public String checkDuplicate(User user){
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            return "This name already exist!";
        }
        if(checkDuplicateEmail(user)!=null){
            return checkDuplicateEmail(user);
        }
        if(checkDuplicatePhone(user)!=null){
            return checkDuplicatePhone(user);
        }
        return null;
    }

    public boolean isEnabled(Integer id) throws UserException {
        return getUserById(id).isEnabled();
    }

    public User getUserById(Integer id) throws UserException {
        return userRepository.findById(id).orElseThrow(()->new UserException("UserNotFound!"));
    }


    public EditUserResponse editUserInformation(EditUserDto editUserDto) {
        try{
            var findUser = userRepository.findByUsername(editUserDto.getUsername());
            if(findUser.isEmpty()){
                throw new NotFoundException("Can't found user");
            }
            var editUser = userRepository.save(
                    User.builder()
                            .id(findUser.get().getId())
                            .username(editUserDto.getUsername())
                            .email(editUserDto.getEmail())
                            .phoneNumber(editUserDto.getPhoneNumber())
                            .updateDate((Date) Calendar.getInstance().getTime())
                            .build()
            );
            return new EditUserResponse(editUser);
        } catch (Exception e){
            throw  new RuntimeException(e);
        }
    }

    public  User updateUser(UserRequest user , Integer id) throws UserException {
        User exUser = getUserById(id);
        String oldMail = exUser.getEmail();
        String oldPhone = exUser.getPhoneNumber();
        exUser.setEmail(user.getEmail());
        exUser.setPhoneNumber(user.getPhoneNumber());

        String checkDuplicationEmail = checkDuplicateEmail(exUser);
        String checkDuplicationPhone = checkDuplicatePhone(exUser);
//        if(checkDuplication!=null && (!user.getEmail().equals(oldMail)||!user.getPhoneNumber().equals(oldPhone))){
//            throw new UserException(checkDuplication);
//        }
        if(checkDuplicationEmail!=null && !user.getEmail().equals(oldMail)){
            throw new UserException(checkDuplicationEmail);
        }
        if(checkDuplicationPhone!=null && !user.getPhoneNumber().equals(oldPhone)){
            throw new UserException(checkDuplicationPhone);
        }
        try{
            userRepository.save(exUser);
        }catch(Exception e){
            return null;
        }
        return exUser;
    }

    public Long getcountUser() {
        return userRepository.count();
    }
    public ResponseObject saveUser(RegisterRequest request) throws UserException{
        User user = User.builder().username(request.getUsername())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .roleName(Role.valueOf(request.getRoleName()))
                .status(UserStatus.Active)
                .password(passwordEncoder.encode(request.getPassword()))
                .createDate(Date.valueOf(LocalDate.now()))
                .updateDate(Date.valueOf(LocalDate.now()))
                .build();
        String checkDuplication = checkDuplicate(user);
        if(checkDuplication!=null){
            throw new UserException(checkDuplication);
        }
        try {
            userRepository.save(user);
            return ResponseObject.builder()
                    .status("SUCCESS")
                    .message("Create user successfully!").build();
        }catch (Exception e){
            return ResponseObject.builder().status("ERROR").message(e.getMessage()).build();
        }
    }
}
