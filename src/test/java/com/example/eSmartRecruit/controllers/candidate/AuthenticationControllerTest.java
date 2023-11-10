package com.example.eSmartRecruit.controllers.candidate;

import com.example.eSmartRecruit.authentication.AuthenticationController;
import com.example.eSmartRecruit.authentication.AuthenticationService;
import com.example.eSmartRecruit.authentication.request_reponse.AuthenticationRequest;
import com.example.eSmartRecruit.authentication.request_reponse.RegisterRequest;
import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.config.JwtService;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.exception.UserException;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.models.enumModel.Role;
import com.example.eSmartRecruit.models.enumModel.UserStatus;
import com.example.eSmartRecruit.repositories.UserRepos;
import com.example.eSmartRecruit.services.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@PrepareForTest(ExtractUser.class)
class AuthenticationControllerTest {
    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private UserRepos userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void register() throws UserException {

        RegisterRequest mockRequest = new RegisterRequest();
        mockRequest.setUsername("hello");
        mockRequest.setPassword("Ab@123");
        mockRequest.setEmail("hello@gmail.com");
        mockRequest.setPhoneNumber("0999999999");
        mockRequest.setRoleName(Role.Candidate.name());

        User mockUser = new User();
        mockUser.setId(4);
        mockUser.setUsername("hello");
        mockUser.setPassword(passwordEncoder.encode("Ab@123"));
        mockUser.setEmail("hello@gmail.com");
        mockUser.setPhoneNumber("0999999999");
        mockUser.setRoleName(Role.Candidate);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf(LocalDate.now()));
        mockUser.setUpdateDate(Date.valueOf(LocalDate.now()));



        var jwtToken = jwtService.generateToken(mockUser);

        ResponseObject response = ResponseObject.builder().message(jwtToken).status("SUCCESS").build();
        lenient().when(authenticationService.register(mockRequest)).thenReturn(response);

        ResponseEntity<ResponseObject> responseEntity = authenticationController.register(mockRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseObject.getStatus());
        assertEquals(jwtToken, responseObject.getMessage());

    }

    @Test
    void authenticateTest() throws UserException {
        AuthenticationRequest mockRequest = new AuthenticationRequest();
        mockRequest.setUsername("hello");
        mockRequest.setPassword("Ab@123");


        User mockUser = new User();
        mockUser.setId(4);
        mockUser.setUsername("hello");
        mockUser.setPassword(passwordEncoder.encode("Ab@123"));
        mockUser.setEmail("hello@gmail.com");
        mockUser.setPhoneNumber("0999999999");
        mockUser.setRoleName(Role.Candidate);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf(LocalDate.now()));
        mockUser.setUpdateDate(Date.valueOf(LocalDate.now()));



        var jwtToken = jwtService.generateToken(mockUser);

        ResponseObject response = ResponseObject.builder().message(jwtToken).status("SUCCESS").build();
        lenient().when(authenticationService.authenticate(mockRequest)).thenReturn(response);

        ResponseEntity<ResponseObject> responseEntity = authenticationController.authentication(mockRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseObject.getStatus());
        assertEquals(jwtToken, responseObject.getMessage());
    }

    @Test
    void enumTest(){
        System.out.println(Role.Candidate.getValue());
        System.out.println(Role.valueOf("Candidate").getValue());
        System.out.println(Role.findRole(1));
    }
}
