package com.example.eSmartRecruit.controllers.interviewer;

import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.config.JwtService;
import com.example.eSmartRecruit.controllers.candidate.CandidateController;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.controllers.request_reponse.request.ReportRequest;
import com.example.eSmartRecruit.exception.InterviewSessionException;
import com.example.eSmartRecruit.exception.UserException;
import com.example.eSmartRecruit.models.InterviewSession;
import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.models.Report;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.models.enumModel.Role;
import com.example.eSmartRecruit.models.enumModel.UserStatus;
import com.example.eSmartRecruit.repositories.ApplicationRepos;
import com.example.eSmartRecruit.repositories.ReportRepos;
import com.example.eSmartRecruit.services.IStorageService;
import com.example.eSmartRecruit.services.impl.ApplicationService;
import com.example.eSmartRecruit.services.impl.InterviewSessionService;
import com.example.eSmartRecruit.services.impl.ReportService;
import com.example.eSmartRecruit.services.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@PrepareForTest({ExtractUser.class})
class InterviewerControllerTest {

    @InjectMocks
    private InterviewerController interviewerController;
    @Mock
    private InterviewSessionService interviewSessionService;
    @Mock
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    private JwtService jwtService;

    @Mock
    private UserService userService;

    @Mock
    private ReportRepos reportRepos;

    @Mock
    private ApplicationRepos applicationRepository;

    @Mock
    private IStorageService storageService;

    @Mock
    private ApplicationService applicationService;
    @Test
    void getInterviewerSession() throws UserException, InterviewSessionException {
        User mockUser = new User();
        mockUser.setId(4);
        mockUser.setUsername("khang");
        mockUser.setPassword("$2a$10$S5x1eUGgsbXA4RJfrnc07ueCheYAVNMXsqw23/HfivFQJsaowrTXW");
        mockUser.setEmail("khang123@gmail.com");
        mockUser.setPhoneNumber(null);
        mockUser.setRoleName(Role.Candidate);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf("2023-11-02"));
        mockUser.setUpdateDate(Date.valueOf("2023-11-02"));

        var jwtToken = jwtService.generateToken(mockUser);

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(4);
        lenient().when(userService.isEnabled(4)).thenReturn(true);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        lenient().when(interviewSessionService.findByInterviewerID(anyInt())).thenReturn(Arrays.asList(new InterviewSession(), new InterviewSession()));
        ResponseEntity<ResponseObject> responseEntity = interviewerController.getInterviewerSession(mockRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseObject.getStatus());
//        assertEquals("Successfully applied!", responseObject.getMessage());

        lenient().when(interviewSessionService.findByInterviewerID(anyInt())).thenThrow(InterviewSessionException.class);
        responseEntity = interviewerController.getInterviewerSession(mockRequest);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());

        responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("ERROR", responseObject.getStatus());

    }

    @Test
    void findByInterviewSessionID() throws InterviewSessionException, UserException {
        User mockUser = new User();
        mockUser.setId(4);
        mockUser.setUsername("khang");
        mockUser.setPassword("$2a$10$S5x1eUGgsbXA4RJfrnc07ueCheYAVNMXsqw23/HfivFQJsaowrTXW");
        mockUser.setEmail("khang123@gmail.com");
        mockUser.setPhoneNumber(null);
        mockUser.setRoleName(Role.Candidate);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf("2023-11-02"));
        mockUser.setUpdateDate(Date.valueOf("2023-11-02"));

        var jwtToken = jwtService.generateToken(mockUser);

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(4);
        lenient().when(userService.isEnabled(4)).thenReturn(true);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        lenient().when(interviewSessionService.findByID(anyInt())).thenReturn(new InterviewSession());
        ResponseEntity<ResponseObject> responseEntity = interviewerController.getInterviewerSession(mockRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseObject.getStatus());
//        assertEquals("Successfully applied!", responseObject.getMessage());

        lenient().when(interviewSessionService.findByInterviewerID(anyInt())).thenThrow(InterviewSessionException.class);
        responseEntity = interviewerController.getInterviewerSession(mockRequest);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());

        responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("ERROR", responseObject.getStatus());
    }

    @Test
    void reportInterviewSession() throws InterviewSessionException, UserException {
        User mockUser = new User();
        mockUser.setId(4);
        mockUser.setUsername("khang");
        mockUser.setPassword("$2a$10$S5x1eUGgsbXA4RJfrnc07ueCheYAVNMXsqw23/HfivFQJsaowrTXW");
        mockUser.setEmail("khang123@gmail.com");
        mockUser.setPhoneNumber(null);
        mockUser.setRoleName(Role.Candidate);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf("2023-11-02"));
        mockUser.setUpdateDate(Date.valueOf("2023-11-02"));

        var jwtToken = jwtService.generateToken(mockUser);

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(4);
        lenient().when(userService.isEnabled(4)).thenReturn(true);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        List<String> stringReturn = Arrays.asList("Successfully saved", "Could not save");

        lenient().when(interviewSessionService.isAlready(1)).thenReturn(true);

        Report rp = new Report(1, "dfsdfa","dgsdfssd",Date.valueOf(LocalDate.now()),Date.valueOf(LocalDate.now()));
        for (String str:stringReturn) {
            when(reportService.reportInterviewSession(rp)).thenReturn(str);
            ResponseEntity<ResponseObject> responseEntity = interviewerController.reportInterviewSession(1, mockRequest, new ReportRequest("dfsdfa","dgsdfssd"));

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals("SUCCESS", responseEntity.getBody().getStatus());
            assertEquals(str, responseEntity.getBody().getMessage());
        }
        rp.setCreateDate(Date.valueOf("2012-12-12"));
        when(reportService.reportInterviewSession(rp)).thenReturn(anyString());
        ResponseEntity<ResponseObject> responseEntity = interviewerController.reportInterviewSession(1, mockRequest, new ReportRequest("dfsdfa","dgsdfssd"));

        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
        assertEquals("ERROR", responseEntity.getBody().getStatus());

    }

    @Test
    void getDetailUserInterviewer() {
    }

    @Test
    void updateUserInterviewer() {
    }

    @Test
    void getCandidateInformation() {
    }
}