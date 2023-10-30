package com.example.eSmartRecruit.authentication;


import com.example.eSmartRecruit.config.JwtService;
import com.example.eSmartRecruit.models.enumModel.Role;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.models.enumModel.UserStatus;
import com.example.eSmartRecruit.repositories.UserRepos;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepos userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        //Role role = Role.valueOf(request.getRoleName());
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
        userRepo.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        System.out.println("before authen");
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
        System.out.println("before user");
        var user = userRepo.findByUsername(request.getUsername()).orElseThrow(RuntimeException::new);
        System.out.println("after user");
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
