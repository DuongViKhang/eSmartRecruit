package com.example.eSmartRecruit.controllers.admin;

import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.config.JwtService;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.models.Application;
import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.models.enumModel.ApplicationStatus;
import com.example.eSmartRecruit.models.enumModel.Role;
import com.example.eSmartRecruit.models.enumModel.UserStatus;
import com.example.eSmartRecruit.repositories.ApplicationRepos;
import com.example.eSmartRecruit.services.impl.PositionService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@PrepareForTest({ExtractUser.class})
class AdminControllerTest {
    @InjectMocks
    private AdminController adminController;

    @Mock
    private ApplicationRepos applicationRepository;

    @Mock
    private UserService userService;

    private JwtService jwtService;

    @Mock
    private PositionService positionService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    public void testGetApplicationSuccess() throws Exception {

        // Mock user
        User mockUser = new User();
        mockUser.setId(6);
        mockUser.setUsername("bichlien02");
        mockUser.setPassword("$2a$10$ZrKkagiTQZc0tcTAO7ETQOFi8S90ZGxC60OJfEuCr9a8G61uLXTdq");
        mockUser.setEmail("bischlien@gmail.com");
        mockUser.setPhoneNumber("0988123421");
        mockUser.setRoleName(Role.Admin);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf("2023-11-07"));
        mockUser.setUpdateDate(Date.valueOf("2023-11-07"));

        var jwtToken = jwtService.generateToken(mockUser);

        // Mock user
        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(6);
        lenient().when(userService.isEnabled(6)).thenReturn(true);
        lenient().when(userService.getUserRole(6)).thenReturn("Admin");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        ///
        Integer applicationId = 1;
        Integer candidateId = 1;
        Integer positionId = 1;
        String candidateName = "John Doe";
        String positionTitle = "Software Engineer";
        // Mock ApplicationRepository
        Application mockApplication = new Application();
        mockApplication.setId(applicationId);
        mockApplication.setCandidateID(candidateId);
        mockApplication.setPositionID(positionId);
        mockApplication.setStatus(ApplicationStatus.valueOf("Pending"));
        mockApplication.setCv("MockCV");
        mockApplication.setCreateDate(Date.valueOf("2023-10-10"));
        lenient().when(applicationRepository.findById(1)).thenReturn(Optional.of(mockApplication));

        //
        User user = new User();
        user.setId(candidateId);
        user.setUsername(candidateName);

        Position position = new Position();
        position.setId(positionId);
        position.setTitle(positionTitle);

        // Mock getUserById(candidateId), PositionService
        lenient().when(userService.getUserById(candidateId)).thenReturn(user);
        lenient().when(positionService.getSelectedPosition(positionId)).thenReturn(position);

        // Call the method
        ResponseEntity<ResponseObject> responseEntity = adminController.getApplication(1, mockRequest);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseEntity.getBody().getStatus());
        assertEquals("Application", responseEntity.getBody().getMessage());
    }

    @Test
    public void testGetApplicationBadRequest() throws Exception {

        // Mock user
        User mockUser = new User();
        mockUser.setId(6);
        mockUser.setUsername("bichlien02");
        mockUser.setPassword("$2a$10$ZrKkagiTQZc0tcTAO7ETQOFi8S90ZGxC60OJfEuCr9a8G61uLXTdq");
        mockUser.setEmail("bischlien@gmail.com");
        mockUser.setPhoneNumber("0988123421");
////////
        mockUser.setRoleName(Role.Candidate);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf("2023-11-07"));
        mockUser.setUpdateDate(Date.valueOf("2023-11-07"));

        var jwtToken = jwtService.generateToken(mockUser);

        // Mock user
        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(6);
////////////
        lenient().when(userService.isEnabled(8)).thenReturn(true);
        lenient().when(userService.getUserRole(6)).thenReturn("Admin");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        ///
        Integer applicationId = 1;
        Integer candidateId = 1;
        Integer positionId = 1;
        String candidateName = "John Doe";
        String positionTitle = "Software Engineer";
        // Mock ApplicationRepository
        Application mockApplication = new Application();
        mockApplication.setId(applicationId);
        mockApplication.setCandidateID(candidateId);
        mockApplication.setPositionID(positionId);
        mockApplication.setStatus(ApplicationStatus.valueOf("Pending"));
        mockApplication.setCv("MockCV");
        mockApplication.setCreateDate(Date.valueOf("2023-10-10"));
        lenient().when(applicationRepository.findById(1)).thenReturn(Optional.of(mockApplication));

        //
        User user = new User();
        user.setId(candidateId);
        user.setUsername(candidateName);

        Position position = new Position();
        position.setId(positionId);
        position.setTitle(positionTitle);

        // Mock getUserById(candidateId), PositionService
        lenient().when(userService.getUserById(candidateId)).thenReturn(user);
        lenient().when(positionService.getSelectedPosition(positionId)).thenReturn(position);

        // Call the method
        ResponseEntity<ResponseObject> responseEntity = adminController.getApplications(mockRequest);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("ERROR", responseEntity.getBody().getStatus());
        assertEquals("Account not active!", responseEntity.getBody().getMessage());
    }

    @Test
    public void testGetApplicationNotFound() throws Exception {

        // Mock user
        User mockUser = new User();
        mockUser.setId(6);
        mockUser.setUsername("bichlien02");
        mockUser.setPassword("$2a$10$ZrKkagiTQZc0tcTAO7ETQOFi8S90ZGxC60OJfEuCr9a8G61uLXTdq");
        mockUser.setEmail("bischlien@gmail.com");
        mockUser.setPhoneNumber("0988123421");
        mockUser.setRoleName(Role.Admin);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf("2023-11-07"));
        mockUser.setUpdateDate(Date.valueOf("2023-11-07"));

        var jwtToken = jwtService.generateToken(mockUser);

        // Mock user
        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(6);
        lenient().when(userService.isEnabled(6)).thenReturn(true);
        lenient().when(userService.getUserRole(6)).thenReturn("Admin");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        ///
        Integer applicationId = 1;
        Integer candidateId = 1;
        Integer positionId = 1;
        String candidateName = "John Doe";
        String positionTitle = "Software Engineer";

        // Mock ApplicationRepository
        Application mockApplication = new Application();
        mockApplication.setId(applicationId);
        mockApplication.setCandidateID(candidateId);
        mockApplication.setPositionID(positionId);
        mockApplication.setStatus(ApplicationStatus.valueOf("Pending"));
        mockApplication.setCv("MockCV");
        mockApplication.setCreateDate(Date.valueOf("2023-10-10"));
///////////////////////////////////////
        lenient().when(applicationRepository.findById(2)).thenReturn(Optional.of(mockApplication));

        //
        User user = new User();
        user.setId(candidateId);
        user.setUsername(candidateName);

        Position position = new Position();
        position.setId(positionId);
        position.setTitle(positionTitle);

        // Mock getUserById(candidateId), PositionService
        lenient().when(userService.getUserById(candidateId)).thenReturn(user);
        lenient().when(positionService.getSelectedPosition(positionId)).thenReturn(position);

        // Call the method
        ResponseEntity<ResponseObject> responseEntity = adminController.getApplication(1, mockRequest);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("ERROR", responseObject.getStatus());
        assertEquals("Application not found", responseObject.getMessage());
    }
    //@Test
    public void testGetApplicationInternalServerError() throws Exception {

        // Mock user
        User mockUser = new User();
        mockUser.setId(6);
        mockUser.setUsername("bichlien02");
        mockUser.setPassword("$2a$10$ZrKkagiTQZc0tcTAO7ETQOFi8S90ZGxC60OJfEuCr9a8G61uLXTdq");
        mockUser.setEmail("bischlien@gmail.com");
        mockUser.setPhoneNumber("0988123421");
        mockUser.setRoleName(Role.Admin);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf("2023-11-07"));
        mockUser.setUpdateDate(Date.valueOf("2023-11-07"));

        var jwtToken = jwtService.generateToken(mockUser);

        // Mock user
        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(6);
        lenient().when(userService.isEnabled(6)).thenReturn(true);
        lenient().when(userService.getUserRole(6)).thenReturn("Admin");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        ///
        Integer applicationId = 1;
        Integer candidateId = 1;
        Integer positionId = 1;
        String candidateName = "John Doe";
        String positionTitle = "Software Engineer";

        // Mock ApplicationRepository
        Application mockApplication = new Application();
        mockApplication.setId(applicationId);
        mockApplication.setCandidateID(candidateId);
        mockApplication.setPositionID(positionId);
        mockApplication.setStatus(ApplicationStatus.valueOf("Pending"));
        mockApplication.setCv("MockCV");
        mockApplication.setCreateDate(Date.valueOf("2023-10-10"));
///////////////////////////////////////
        lenient().when(applicationRepository.findById(2)).thenReturn(Optional.of(mockApplication));

        //
        User user = new User();
        user.setId(candidateId);
        user.setUsername(candidateName);

        Position position = new Position();
        position.setId(positionId);
        position.setTitle(positionTitle);

        // Mock getUserById(candidateId), PositionService
        lenient().when(userService.getUserById(candidateId)).thenReturn(user);
        lenient().when(positionService.getSelectedPosition(positionId)).thenReturn(position);

        // Call the method
        ResponseEntity<ResponseObject> responseEntity = adminController.getApplication(1, mockRequest);

        // Assertions
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("ERROR", responseObject.getStatus());
        assertEquals("Internal server error", responseObject.getMessage());
    }



/// test get applicationsss
@Test
public void testGetApplicationsSuccess() throws Exception {

    // Mock user
    User mockUser = new User();
    mockUser.setId(6);
    mockUser.setUsername("bichlien02");
    mockUser.setPassword("$2a$10$ZrKkagiTQZc0tcTAO7ETQOFi8S90ZGxC60OJfEuCr9a8G61uLXTdq");
    mockUser.setEmail("bischlien@gmail.com");
    mockUser.setPhoneNumber("0988123421");
    mockUser.setRoleName(Role.Admin);
    mockUser.setStatus(UserStatus.Active);
    mockUser.setCreateDate(Date.valueOf("2023-11-07"));
    mockUser.setUpdateDate(Date.valueOf("2023-11-07"));

    var jwtToken = jwtService.generateToken(mockUser);

    // Mock user
    ExtractUser mockUserInfo = mock(ExtractUser.class);
    lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
    lenient().when(mockUserInfo.getUserId()).thenReturn(6);
    lenient().when(userService.isEnabled(6)).thenReturn(true);
    lenient().when(userService.getUserRole(6)).thenReturn("Admin");
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

    ///
    Integer applicationId = 1;
    Integer candidateId = 1;
    Integer positionId = 1;
    String candidateName = "John Doe";
    String positionTitle = "Software Engineer";
    // Mock ApplicationRepository
    List<Application> mockApplications = new ArrayList<>();
    Application application = new Application();
    application.setId(applicationId);
    application.setCandidateID(candidateId);
    application.setPositionID(positionId);
    application.setStatus(ApplicationStatus.valueOf("Pending"));
    application.setCv("MockCV");
    application.setCreateDate(Date.valueOf("2023-10-10"));
    mockApplications.add(application);

    lenient().when(applicationRepository.findAll()).thenReturn(mockApplications);

    //
    User user = new User();
    user.setId(candidateId);
    user.setUsername(candidateName);

    Position position = new Position();
    position.setId(positionId);
    position.setTitle(positionTitle);

    // Mock getUserById(candidateId), PositionService
    lenient().when(userService.getUserById(candidateId)).thenReturn(user);
    lenient().when(positionService.getSelectedPosition(positionId)).thenReturn(position);

    // Call the method
    ResponseEntity<ResponseObject> responseEntity = adminController.getApplications(mockRequest);

    // Assertions
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    ResponseObject responseObject = responseEntity.getBody();
    assertNotNull(responseObject);
    assertEquals("SUCCESS", responseEntity.getBody().getStatus());
    assertEquals("Applications retrieved successfully.", responseEntity.getBody().getMessage());
}

    @Test
    public void testGetApplicationsBadRequest() throws Exception {

        // Mock user
        User mockUser = new User();
        mockUser.setId(6);
        mockUser.setUsername("bichlien02");
        mockUser.setPassword("$2a$10$ZrKkagiTQZc0tcTAO7ETQOFi8S90ZGxC60OJfEuCr9a8G61uLXTdq");
        mockUser.setEmail("bischlien@gmail.com");
        mockUser.setPhoneNumber("0988123421");
////////
        mockUser.setRoleName(Role.Candidate);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf("2023-11-07"));
        mockUser.setUpdateDate(Date.valueOf("2023-11-07"));

        var jwtToken = jwtService.generateToken(mockUser);

        // Mock user
        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(6);
////////////
        lenient().when(userService.isEnabled(8)).thenReturn(true);
        lenient().when(userService.getUserRole(6)).thenReturn("Admin");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        ///
        Integer applicationId = 1;
        Integer candidateId = 1;
        Integer positionId = 1;
        String candidateName = "John Doe";
        String positionTitle = "Software Engineer";
        // Mock ApplicationRepository
        Application mockApplication = new Application();
        mockApplication.setId(applicationId);
        mockApplication.setCandidateID(candidateId);
        mockApplication.setPositionID(positionId);
        mockApplication.setStatus(ApplicationStatus.valueOf("Pending"));
        mockApplication.setCv("MockCV");
        mockApplication.setCreateDate(Date.valueOf("2023-10-10"));
        lenient().when(applicationRepository.findById(1)).thenReturn(Optional.of(mockApplication));

        //
        User user = new User();
        user.setId(candidateId);
        user.setUsername(candidateName);

        Position position = new Position();
        position.setId(positionId);
        position.setTitle(positionTitle);

        // Mock getUserById(candidateId), PositionService
        lenient().when(userService.getUserById(candidateId)).thenReturn(user);
        lenient().when(positionService.getSelectedPosition(positionId)).thenReturn(position);

        // Call the method
        ResponseEntity<ResponseObject> responseEntity = adminController.getApplication(1, mockRequest);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("ERROR", responseEntity.getBody().getStatus());
        assertEquals("Account not active!", responseEntity.getBody().getMessage());
    }

}