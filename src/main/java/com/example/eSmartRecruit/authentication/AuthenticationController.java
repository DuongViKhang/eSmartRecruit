package com.example.eSmartRecruit.authentication;


import com.example.eSmartRecruit.authentication.request_reponse.AuthenticationRequest;
import com.example.eSmartRecruit.authentication.request_reponse.AuthenticationResponse;
import com.example.eSmartRecruit.authentication.request_reponse.RegisterRequest;

import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.exception.UserException;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.services.impl.UserService;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eSmartRecruit")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(
            @RequestBody @Valid RegisterRequest request
    ) throws UserException {
        try {
            return ResponseEntity.ok(authenticationService.register(request));
        }
        catch (Exception exception){
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("ERROR").message(exception.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/auth")
    public ResponseEntity<ResponseObject> authentication(
            @RequestBody @Valid @Validated AuthenticationRequest request
    ){
        try{
            return ResponseEntity.ok(authenticationService.authenticate(request));
        }catch (Exception exception){
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("ERROR").message(exception.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication) {
        // Đăng xuất người dùng và xóa phiên làm việc
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, authentication);
        return ResponseEntity.ok("{\"status\": \"SUCCESS\", \"message\": \"Sign out successfully!\"}");
    }
    @GetMapping("/hello")
    public List<User> sayHello() throws UserException {
        System.out.println("get hello");
        return userService.getAllUser();
    }
}
