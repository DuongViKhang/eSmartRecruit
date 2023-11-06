package com.example.eSmartRecruit.authentication;



import com.example.eSmartRecruit.authentication.request_reponse.AuthenticationRequest;
import com.example.eSmartRecruit.authentication.request_reponse.AuthenticationResponse;
import com.example.eSmartRecruit.authentication.request_reponse.RegisterRequest;

import com.example.eSmartRecruit.config.JwtService;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.exception.UserException;
import com.example.eSmartRecruit.models.enumModel.Role;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.models.enumModel.UserStatus;
import com.example.eSmartRecruit.repositories.UserRepos;
import com.example.eSmartRecruit.services.impl.TokenService;
import com.example.eSmartRecruit.services.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.yaml.snakeyaml.util.EnumUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepos userRepo;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    public ResponseObject register(RegisterRequest request) throws UserException {
        //Role role = Role.valueOf(request.getRoleName());
        for (Role value:Role.values()) {
            if(value.toString().equals(request.getRoleName())){
                break;
            }
            return ResponseObject.builder().message("Wrong Role name").build();
        }
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .roleName(Role.valueOf(request.getRoleName()))
                .status(UserStatus.Active)
                .password(passwordEncoder.encode(request.getPassword()))
                .createDate(Date.valueOf(LocalDate.now()))
                .updateDate(Date.valueOf(LocalDate.now()))
                .build();
        String checkDuplication = userService.checkDuplicate(user);
        if(checkDuplication!=null){
            throw new UserException(checkDuplication);
        }
        try {
            userRepo.save(user);
        }catch (Exception e){
            return ResponseObject.builder().status("ERROR").message(e.getMessage()).build();
        }
        var jwtToken = jwtService.generateToken(user);

        // Lưu token vào cơ sở dữ liệu
        tokenService.saveToken(user.getUsername(), jwtToken);

        return ResponseObject.builder()
                .status("SUCCESS")
                .message(jwtToken)
                .build();
    }

    public ResponseObject authenticate(AuthenticationRequest request) throws UserException {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword())

            );

            var user = userRepo.findByUsername(request.getUsername()).orElseThrow(()->new UserException("User not found!"));
            if (!user.isEnabled()){
                return ResponseObject.builder()
                        .message("Account not active")
                        .build();
            }
            var jwtToken = jwtService.generateToken(user);

            // Lưu token vào cơ sở dữ liệu
            tokenService.saveToken(user.getUsername(), jwtToken);

            return ResponseObject.builder()
                    .message(jwtToken)
                    .status("SUCCESS")
                    .build();
        }catch (Exception exception){
            throw new UserException("User name or password is wrong");
        }
    }

}
