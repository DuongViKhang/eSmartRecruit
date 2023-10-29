package com.example.eSmartRecruit.authentication;

import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.services.impl.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eSmartRecruit")
@RequiredArgsConstructor
public class Authentication {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthenticationResponse> authentication(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/hello")
    public List<User> sayHello(){
        System.out.println("get hello");
        return userService.getAllUser();
    }
}
