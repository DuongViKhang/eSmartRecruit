package com.example.eSmartRecruit.services.impl;

import com.example.eSmartRecruit.authentication.AuthenticationRequest;
import com.example.eSmartRecruit.authentication.request_reponse.AuthenticationResponse;
import com.example.eSmartRecruit.config.JwtService;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.repositories.UserRepos;
import com.example.eSmartRecruit.services.IAuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationService implements IAuthenticationService {
    private JwtService _jwtService;
    private UserRepos _userRepos;
    private final AuthenticationManager _authenticationManager;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        _authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));

        Optional<User> user = _userRepos.findByUsername(request.getEmail());

        if (user.isPresent()){
            User _user = user.get();

            String token = _jwtService.generateToken(_user);


            return AuthenticationResponse.builder()
                    .message("Login Successfully")
                    .token(token)
                    .build();
        }

        return AuthenticationResponse.builder().message("Username or Password is incorrect ").token("").build();
    }

}
