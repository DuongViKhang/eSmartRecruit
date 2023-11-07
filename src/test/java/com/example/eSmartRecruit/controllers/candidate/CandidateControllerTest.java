package com.example.eSmartRecruit.controllers.candidate;

import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.config.JwtService;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.controllers.request_reponse.request.UserRequest;
import com.example.eSmartRecruit.exception.PositionException;
import com.example.eSmartRecruit.exception.UserException;
import com.example.eSmartRecruit.models.Application;
import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.models.enumModel.ApplicationStatus;
import com.example.eSmartRecruit.models.enumModel.Role;
import com.example.eSmartRecruit.models.enumModel.UserStatus;
import com.example.eSmartRecruit.repositories.ApplicationRepos;
import com.example.eSmartRecruit.repositories.UserRepos;
import com.example.eSmartRecruit.services.IStorageService;
import com.example.eSmartRecruit.services.impl.ApplicationService;
import com.example.eSmartRecruit.services.impl.PositionService;
import com.example.eSmartRecruit.services.impl.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;

@ExtendWith(MockitoExtension.class)
@PrepareForTest({ExtractUser.class})
class CandidateControllerTest {
    @InjectMocks
    private CandidateController candidateController;

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
    @Test
    void home() {
        List<Position> mockPositions = new ArrayList<>();

        Position position1 = new Position();
        position1.setId(1);
        position1.setTitle("Software Engineer");
        position1.setJobDescription("Build web applications");
        position1.setSalary(BigDecimal.valueOf(5000));
        position1.setPostDate(Date.valueOf("2023-10-10"));
        position1.setExpireDate(Date.valueOf("2023-10-30"));
        position1.setUpdateDate(Date.valueOf("2023-10-10"));
        position1.setLocation("FPT, Thu Duc City");
        mockPositions.add(position1);

        Position position2 = new Position();
        position2.setId(2);
        position2.setTitle("Security Engineer");
        position2.setJobDescription("Responsible for web application security");
        position2.setSalary(BigDecimal.valueOf(5000));
        position2.setPostDate(Date.valueOf("2023-10-10"));
        position2.setExpireDate(Date.valueOf("2023-10-30"));
        position2.setUpdateDate(Date.valueOf("2023-10-10"));
        position2.setLocation("FPT, Thu Duc City");
        mockPositions.add(position2);

        when(positionService.getAllPosition()).thenReturn(mockPositions);

        ResponseEntity<ResponseObject> responseEntity = candidateController.home();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseObject.getStatus());
        assertEquals("List position successfully!", responseObject.getMessage());

        List<Position> returnedData = (List<Position>) responseObject.getData();
        assertNotNull(returnedData);
        assertEquals(2, returnedData.size());

        verify(positionService, times(1)).getAllPosition();
    }

    @Test
    void getDetailPosition() throws PositionException {
        Position mockPosition = new Position();
        mockPosition.setId(1);
        mockPosition.setTitle("Software Engineer");
        mockPosition.setJobDescription("Build web applications");
        mockPosition.setJobRequirements("3 years of experience");
        mockPosition.setSalary(BigDecimal.valueOf(5000));
        mockPosition.setPostDate(Date.valueOf("2023-10-10"));
        mockPosition.setExpireDate(Date.valueOf("2023-10-30"));
        mockPosition.setUpdateDate(Date.valueOf("2023-10-10"));
        mockPosition.setLocation("FPT, Thu Duc City");

        when(positionService.getSelectedPosition(1)).thenReturn(mockPosition);
        ResponseEntity<ResponseObject> responseEntity = candidateController.getDetailPosition(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseObject.getStatus());

        Position returnedPosition = (Position) responseObject.getData();
        assertNotNull(returnedPosition);
        assertEquals(1, returnedPosition.getId());
        assertEquals("Software Engineer", returnedPosition.getTitle());

        verify(positionService, times(1)).getSelectedPosition(1);
    }

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }
    @Test
    void applyForPosition() throws Exception{

        MockMultipartFile mockFile = new MockMultipartFile("cv", "cv.pdf", "application/pdf", "cv data".getBytes());

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
        lenient().when(storageService.storeFile(mockFile)).thenReturn("generatedFileName");
        lenient().when(positionService.isPresent(1)).thenReturn(true);
        lenient().when(applicationService.apply(any(Application.class))).thenReturn("Successfully applied");

        ResponseEntity<ResponseObject> responseEntity = candidateController.applyForPosition(1, mockRequest, mockFile);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseObject.getStatus());
        assertEquals("Successfully applied!", responseObject.getMessage());

        verify(storageService, times(1)).storeFile(mockFile);
        verify(positionService, times(1)).isPresent(1);
        verify(applicationService, times(1)).apply(any(Application.class));
    }

    @Test
    void getMyApplications() throws JSONException, UserException, PositionException {
//
//        User mockUser = new User();
//        mockUser.setId(4);
//        mockUser.setUsername("khang");
//        mockUser.setPassword("$2a$10$S5x1eUGgsbXA4RJfrnc07ueCheYAVNMXsqw23/HfivFQJsaowrTXW");
//        mockUser.setEmail("khang123@gmail.com");
//        mockUser.setPhoneNumber(null);
//        mockUser.setRoleName(Role.Candidate);
//        mockUser.setStatus(UserStatus.Active);
//        mockUser.setCreateDate(Date.valueOf("2023-11-02"));
//        mockUser.setUpdateDate(Date.valueOf("2023-11-02"));
//
//        var jwtToken = jwtService.generateToken(mockUser);
//
//        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
//        when(mockRequest.getHeader("Authorization")).thenReturn(jwtToken);
//
//        ExtractUser mockUserInfo = mock(ExtractUser.class);
//
//        when(mockUserInfo.isEnabled()).thenReturn(true);
//        when(mockUserInfo.getUserId()).thenReturn(4);
//        List<Application> mockApplications = new ArrayList<>();
//        Application mockApp1 = new Application();
//        mockApp1.setId(1);
//        mockApp1.setCandidateID(4);
//        mockApp1.setPositionID(1);
//        mockApp1.setStatus(ApplicationStatus.Pending);
//        mockApp1.setCv("abc");
//        mockApp1.setCreateDate(Date.valueOf("2023-11-5"));
//        mockApp1.setUpdateDate(Date.valueOf("2023-11-5"));
//        mockApplications.add(mockApp1);
//
//        Application mockApp2 = new Application();
//        mockApp2.setId(1);
//        mockApp2.setCandidateID(4);
//        mockApp2.setPositionID(2);
//        mockApp2.setStatus(ApplicationStatus.Pending);
//        mockApp2.setCv("abc");
//        mockApp2.setCreateDate(Date.valueOf("2023-11-5"));
//        mockApp2.setUpdateDate(Date.valueOf("2023-11-5"));
//        mockApplications.add(mockApp2);
//
//        List<Position> mockPositions = new ArrayList<>();
//        Position mockPos1 = new Position();
//        mockPos1.setId(1);
//        mockPos1.setTitle("Software Engineer");
//        mockPos1.setJobDescription("Abc");
//        mockPos1.setJobRequirements("Abc");
//        mockPos1.setSalary(BigDecimal.valueOf(5000));
//        mockPos1.setPostDate(Date.valueOf("2023-11-5"));
//        mockPos1.setExpireDate(Date.valueOf("2023-11-15"));
//        mockPos1.setLocation("FTP");
//        mockPositions.add(mockPos1);
//
//        Position mockPos2 = new Position();
//        mockPos2.setId(2);
//        mockPos2.setTitle("Security Engineer");
//        mockPos2.setJobDescription("Abc");
//        mockPos2.setJobRequirements("Abc");
//        mockPos2.setSalary(BigDecimal.valueOf(5000));
//        mockPos2.setPostDate(Date.valueOf("2023-11-5"));
//        mockPos2.setExpireDate(Date.valueOf("2023-11-15"));
//        mockPos2.setLocation("FTP");
//        mockPositions.add(mockPos2);
//
//        when(userService.getUserById(4)).thenReturn(mockUser);
//        when(applicationRepository.findByCandidateID(4)).thenReturn(mockApplications);
//        when(positionService.getSelectedPosition(2)).thenReturn(mockPos2);
//        when(positionService.isPresent(2)).thenReturn(true);
//
//        ResponseEntity<ResponseObject> responseEntity = candidateController.getMyApplications(mockRequest);
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//
//        ResponseObject responseObject = responseEntity.getBody();
//        assertNotNull(responseObject);
//        assertEquals("Success", responseObject.getStatus());
//
//        List<Map<String, Object>> applicationList = (List<Map<String, Object>>) responseObject.getData();
//        assertNotNull(applicationList);
//        assertEquals(2, applicationList.size());
//
//        Map<String, Object> applicationMap = applicationList.get(0);
//        assertNotNull(applicationMap);
//        assertEquals(1, applicationMap.get("applicationID"));
//        assertEquals("Software Engineer", applicationMap.get("positionTitle"));
//        assertEquals("Pending", applicationMap.get("status"));
//        assertNotNull(applicationMap.get("applicationDate"));

    }

    @Test
    void getApplicationDetails() {
    }

    @Test
    void getDetailUser() {
    }

    @Test
    void updateUser() {
    }
}