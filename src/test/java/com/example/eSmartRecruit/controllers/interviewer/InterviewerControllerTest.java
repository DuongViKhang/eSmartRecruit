package com.example.eSmartRecruit.controllers.interviewer;

import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.config.JwtService;
import com.example.eSmartRecruit.controllers.candidate.CandidateController;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.controllers.request_reponse.request.UserRequest;
import com.example.eSmartRecruit.exception.UserException;
import com.example.eSmartRecruit.models.Application;
import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.models.enumModel.ApplicationStatus;
import com.example.eSmartRecruit.models.enumModel.Role;
import com.example.eSmartRecruit.models.enumModel.UserStatus;
import com.example.eSmartRecruit.repositories.ApplicationRepos;
import com.example.eSmartRecruit.services.IStorageService;
import com.example.eSmartRecruit.services.impl.ApplicationService;
import com.example.eSmartRecruit.services.impl.PositionService;
import com.example.eSmartRecruit.services.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@PrepareForTest({ExtractUser.class})
class InterviewerControllerTest {

    @InjectMocks
    private InterviewerController interviewerController;

    @Mock
    private PositionService positionService;

    @Mock
    private UserService userService;

    @Mock
    private ApplicationRepos applicationRepository;

    @Mock
    private IStorageService storageService;

    @Mock
    private ApplicationService applicationService;

    private JwtService jwtService;
    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void getDetailUserInterviewer() throws UserException , JSONException{
        User mockUser = new User();
        mockUser.setId(4);
        mockUser.setUsername("thang");
        mockUser.setPassword("$2a$10$UlDZ./Oc2vM.mhpQ/zYPF.r3OKFahBNqe5MekYaCeLdqMrElTgtAO");
        mockUser.setEmail("abc123@gmail.com");
        mockUser.setPhoneNumber("0988888889");
        mockUser.setRoleName(Role.Interviewer);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf("2023-11-07"));
        mockUser.setUpdateDate(Date.valueOf("2023-11-07"));

        var jwtToken = jwtService.generateToken(mockUser);
        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(4);
        when(userService.isEnabled(4)).thenReturn(true);
        when(userService.getUserById(4)).thenReturn(mockUser);
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        ResponseEntity<ResponseObject> responseEntity = interviewerController.getDetailUserInterviewer(mockRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        Map<String, String> data = new LinkedHashMap<>();
        data.put("username", "thang");
        data.put("email", "abc123@gmail.com");
        data.put("phonenumber", "0988888889");
        data.put("roleName", "Interviewer");
        assertEquals("Success", responseObject.getStatus());
//        assertEquals("Loading data success!", responseObject.getMessage());
        assertEquals(data, responseObject.getData());
    }

    @Test
    void updateUserInterviewer() throws UserException, JSONException {
        User mockUser = new User();
        mockUser.setId(4);
        mockUser.setUsername("thang");
        mockUser.setPassword("$2a$10$UlDZ./Oc2vM.mhpQ/zYPF.r3OKFahBNqe5MekYaCeLdqMrElTgtAO");
        mockUser.setEmail("abc123@gmail.com");
        mockUser.setPhoneNumber("0988888889");
        mockUser.setRoleName(Role.Interviewer);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf("2023-11-07"));
        mockUser.setUpdateDate(Date.valueOf("2023-11-07"));

        User updateMockUser = new User();
        updateMockUser.setId(4);
        updateMockUser.setUsername("thang1");
        updateMockUser.setPassword("$2a$10$UlDZ./Oc2vM.mhpQ/zYPF.r3OKFahBNqe5MekYaCeLdqMrElTgtAO");
        updateMockUser.setEmail("thang123@gmail.com");
        updateMockUser.setPhoneNumber("0977888889");
        updateMockUser.setRoleName(Role.Interviewer);
        updateMockUser.setStatus(UserStatus.Active);
        updateMockUser.setCreateDate(Date.valueOf("2023-11-07"));
        updateMockUser.setUpdateDate(Date.valueOf("2023-11-07"));

        var jwtToken = jwtService.generateToken(mockUser);

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(4);
        lenient().when(userService.isEnabled(4)).thenReturn(true);
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        var userId = mockUserInfo.getUserId();
        var userRequest = new UserRequest("thang123@gmail.com","0977888889");
        lenient().when(userService.updateUser(userRequest,userId)).thenReturn(updateMockUser);
        ResponseEntity<ResponseObject> responseEntity = interviewerController.updateUserInterviewer(mockRequest,userRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("username", updateMockUser.getUsername());
        data.put("email", updateMockUser.getEmail());
        data.put("phoneNumber",updateMockUser.getPhoneNumber());

        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("Success", responseObject.getStatus());
        assertEquals(data, responseObject.getData());
    }

    @Test
    void getCandidateInformation() throws UserException, JSONException{

        User mockUser = new User();
        mockUser.setId(5);
        mockUser.setUsername("thang1");
        mockUser.setPassword("$2a$10$T7/ttTcvqqo3pFkqztnTmODYJvpPeHIjyUPmeKlkjwW8XSRG6q/CK");
        mockUser.setEmail("thang1234@gmail.com");
        mockUser.setPhoneNumber("0999996789");
        mockUser.setRoleName(Role.Candidate);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf("2023-11-08"));
        mockUser.setUpdateDate(Date.valueOf("2023-11-08"));

        var jwtToken = jwtService.generateToken(mockUser);
        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(4);
        when(userService.isEnabled(5)).thenReturn(true);
        when(userService.getUserById(5)).thenReturn(mockUser);
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        ResponseEntity<ResponseObject> responseEntity = interviewerController.getCandidateInformation(5,mockRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        Map<String, String> data = new LinkedHashMap<>();
        data.put("username", "thang1");
        data.put("email", "thang1234@gmail.com");
        data.put("phonenumber", "0999996789");
        data.put("roleName",Role.Candidate.toString());

        assertEquals("Success", responseObject.getStatus());
        assertEquals(data, responseObject.getData());

    }
}