package com.example.eSmartRecruit.controllers.admin;
import com.example.eSmartRecruit.authentication.request_reponse.RegisterRequest;
import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.config.JwtService;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.controllers.request_reponse.request.PositionRequest;
import com.example.eSmartRecruit.exception.InterviewSessionException;
import com.example.eSmartRecruit.exception.PositionException;
import com.example.eSmartRecruit.exception.UserException;
import com.example.eSmartRecruit.models.Application;
import com.example.eSmartRecruit.models.InterviewSession;
import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.models.enumModel.ApplicationStatus;
import com.example.eSmartRecruit.models.enumModel.Role;
import com.example.eSmartRecruit.models.enumModel.SessionStatus;
import com.example.eSmartRecruit.models.enumModel.UserStatus;
import com.example.eSmartRecruit.repositories.ApplicationRepos;
import com.example.eSmartRecruit.services.IStorageService;
import com.example.eSmartRecruit.services.impl.ApplicationService;
import com.example.eSmartRecruit.services.impl.InterviewSessionService;
import com.example.eSmartRecruit.services.impl.PositionService;
import com.example.eSmartRecruit.services.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@ExtendWith(MockitoExtension.class)
@PrepareForTest({ExtractUser.class})
class AdminControllerTest {
    @InjectMocks
    private AdminController adminController;

    private JwtService jwtService;
    @Mock
    private IStorageService storageService;
    @Mock
    private UserService userService;

    @Mock
    private ApplicationService applicationService;
    @Mock
    private ApplicationRepos applicationRepository;


    @Mock
    private PositionService positionService;

    @Mock
    InterviewSessionService interviewSessionService;
    private HttpServletRequest mockRequest;



    @BeforeEach
    void setUp() throws UserException {

        User mockUser = new User();
        mockUser.setId(2);
        mockUser.setUsername("bcd");
        mockUser.setPassword("$2a$10$SgZX47bsE057V9z4n1NeG.y0hJkv1scG07pmPjPmBovIAnw4RhB7y");
        mockUser.setEmail("b123@gmail.com");
        mockUser.setPhoneNumber("0988888888");
        mockUser.setRoleName(Role.Admin);
        mockUser.setStatus(UserStatus.Inactive);
        mockUser.setCreateDate(Date.valueOf("2023-10-23"));
        mockUser.setUpdateDate(Date.valueOf("2023-10-23"));

        jwtService = new JwtService();
        jwtToken = jwtService.generateToken(mockUser);
        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(true);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
    }
    private String jwtToken;
    @Test
    void getPosition_shouldThrowUserExceptionWhenAccountNotFound1()  throws UserException{

        ResponseEntity<ResponseObject> responseEntity = adminController.getPositionAdmin(mockRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("ERROR", responseObject.getStatus());
    }
    @Test
    void getPosition_shouldThrowUserExceptionWhenAccountNotFound() throws UserException {

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(false);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(false);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        ResponseEntity<ResponseObject> responseEntity = adminController.getPositionAdmin(mockRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("ERROR", responseObject.getStatus());
        assertEquals("Account is not active!", responseObject.getMessage());
    }

    @Test
    void getPosition_shouldReturnErrorWhenPositionNotFound() throws PositionException {

        lenient().when(positionService.getAllPosition()).thenReturn(null);
        // Act
        ResponseEntity<ResponseObject> responseEntity = adminController.getPositionAdmin(mockRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("ERROR", responseObject.getStatus());
    }

    @Test
    void getPosition_shouldReturnSuccessWithPositionList() throws UserException, PositionException {
        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(true);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        List<Position> mockPositions = new ArrayList<>();
        Position pos = new Position();
        pos.setId(1);
        pos.setTitle("Front-end Dev");
        pos.setJobDescription("abc");
        pos.setJobRequirements("abc");
        pos.setSalary(BigDecimal.valueOf(1000.00));
        pos.setPostDate(Date.valueOf("2023-10-26"));
        pos.setExpireDate(Date.valueOf("2023-10-26"));
        pos.setUpdateDate(null);
        pos.setLocation("fpt");
        mockPositions.add(pos);

        Position poss = new Position();
        poss.setId(2);
        poss.setTitle("Back-end Dev");
        poss.setJobDescription("bcd");
        poss.setJobRequirements("bcd");
        poss.setSalary(BigDecimal.valueOf(2000.00));
        poss.setPostDate(Date.valueOf("2023-10-25"));
        poss.setExpireDate(Date.valueOf("2023-10-25"));
        poss.setUpdateDate(null);
        poss.setLocation("fpt");
        mockPositions.add(poss);

        when(positionService.getAllPosition()).thenReturn(mockPositions);
        ResponseEntity<ResponseObject> responseEntity = adminController.getPositionAdmin(mockRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseObject.getStatus());
        assertEquals("Loading position successfully", responseObject.getMessage());
        assertEquals(mockPositions, responseObject.getData());
    }

    //
    @Test
    void getDetailPosition_shouldReturnSuccessWithPositionList() throws PositionException, UserException {

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(true);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        Position mockPosition = new Position();
        mockPosition.setId(1);
        mockPosition.setTitle("Front-end Dev");
        mockPosition.setJobDescription("abc");
        mockPosition.setJobRequirements("abc");
        mockPosition.setSalary(BigDecimal.valueOf(1000.00));
        mockPosition.setPostDate(Date.valueOf("2023-10-26"));
        mockPosition.setExpireDate(Date.valueOf("2023-10-26"));
        mockPosition.setUpdateDate(null);
        mockPosition.setLocation("fpt");

        when(positionService.getSelectedPosition(1)).thenReturn(mockPosition);
        ResponseEntity<ResponseObject> responseEntity = adminController.getDetailPositionAdmin(1, mockRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseObject.getStatus());
        assertEquals("Loading position successfully", responseObject.getMessage());
        Position returnedPosition = (Position) responseObject.getData();
        assertNotNull(returnedPosition);
        assertEquals(1, returnedPosition.getId());
        assertEquals("Front-end Dev", returnedPosition.getTitle());

        verify(positionService, times(1)).getSelectedPosition(1);
    }

    //
    @Test
    void getDetailPositionAdmin_shouldReturnErrorWhenPositionNotFound() throws PositionException, UserException {

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(true);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        lenient().when(positionService.getSelectedPosition(1)).thenReturn(null);
        // Act
        ResponseEntity<ResponseObject> responseEntity = adminController.getDetailPositionAdmin(1, mockRequest);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("ERROR", responseObject.getStatus());
        assertNull(responseObject.getData());
    }

    //
    @Test
    void getDetailPosition_shouldReturnErrorWithPositionList() throws PositionException, UserException {

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(false);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(false);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        ResponseEntity<ResponseObject> responseEntity = adminController.getDetailPositionAdmin(1, mockRequest);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertEquals("ERROR", responseObject.getStatus());
        assertEquals("Account is not active!", responseObject.getMessage());

    }

    @Test
    void home_shouldReturnSuccessWithHomeData() throws UserException, JSONException {

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(true);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        when(userService.getcountUser()).thenReturn(Long.valueOf(10));
        when(positionService.getcountPosition()).thenReturn(Long.valueOf(5));
        when(applicationService.getcountApplication()).thenReturn(Long.valueOf(20));
        when(interviewSessionService.getCountInterview()).thenReturn(Long.valueOf(3));

        // Gọi hàm home()
        ResponseEntity<ResponseObject> response = adminController.home(mockRequest);
        // Kiểm tra kết quả
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getStatus());

        verify(userService).getcountUser();
        verify(positionService).getcountPosition();
        verify(applicationService).getcountApplication();
        verify(interviewSessionService).getCountInterview();
    }

    @Test
    void getUsers_shouldReturnSuccessWithUserList() throws JSONException, UserException {

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(true);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        List<User> mockUserList = new ArrayList<>();
        mockUserList.add(User.builder().id(3)
                .username("tien").password("Tien123")
                .email("tien123@gmail.com").phoneNumber(null)
                .roleName(Role.Candidate).status(UserStatus.Active)
                .createDate(Date.valueOf("2023-11-11"))
                .updateDate(Date.valueOf("2023-11-11")).build());
        mockUserList.add(User.builder().id(4)
                .username("a").password("a123")
                .email("a123@gmail.com").phoneNumber(null)
                .roleName(Role.Candidate).status(UserStatus.Active)
                .createDate(Date.valueOf("2023-11-11"))
                .updateDate(Date.valueOf("2023-11-11")).build());
        when(userService.getAllUser()).thenReturn(mockUserList);
        ResponseEntity<ResponseObject> response = adminController.getUsers(mockRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SUCCESS", response.getBody().getStatus());
        assertEquals("List all users successfully!", response.getBody().getMessage());

        List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.getBody().getData();
        assertEquals(mockUserList.size(), dataList.size());

        for (int i = 0; i < mockUserList.size(); i++) {
            User user = mockUserList.get(i);
            Map<String, Object> data = dataList.get(i);

            assertEquals(user.getId(), data.get("id"));
            assertEquals(user.getUsername(), data.get("username"));
            assertEquals(user.getEmail(), data.get("email"));
            assertEquals(user.getPhoneNumber(), data.get("phonenumber"));
            assertEquals(user.getRoleName(), data.get("rolename"));
            assertEquals(user.getStatus(), data.get("status"));
            assertEquals(user.getCreateDate(), data.get("create_date"));
            assertEquals(user.getUpdateDate(), data.get("update_date"));
        }
    }

    @Test
    void getUsers_shouldReturnForbiddenWhenUserNotEnabled() throws JSONException, UserException {

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(false);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(false);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        ResponseEntity<ResponseObject> response = adminController.getUsers(mockRequest);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void getUsers_shouldReturnForbiddenWhenUserIsNotAdmin() throws JSONException, UserException {

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(true);
        lenient().when(userService.getUserRole(2)).thenReturn("Candidate");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        ResponseEntity<ResponseObject> response = adminController.getUsers(mockRequest);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void getUsers_shouldReturnInternalServerErrorResponseWhenCatchUserException() throws JSONException, UserException {

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(true);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");
        when(userService.getAllUser()).thenThrow(new UserException("Error getting users"));
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        ResponseEntity<ResponseObject> response = adminController.getUsers(mockRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR", response.getBody().getStatus());
    }

    @Test
    void createUser_shouldReturnForbiddenResponseWhenUserNotEnabled() throws UserException {

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(false);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(false);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        RegisterRequest registerRequest = new RegisterRequest();
        lenient().when(userService.saveUser(registerRequest)).thenReturn(ResponseObject.builder()
                .status("SUCCESS").message("Create user successfully!").build());

        ResponseEntity<ResponseObject> responseEntity = adminController.createUser(mockRequest, registerRequest);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void createUser_shouldReturnForbiddenWhenUserIsNotAdmin() throws JSONException, UserException {

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(true);
        lenient().when(userService.getUserRole(2)).thenReturn("Candidate");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        RegisterRequest registerRequest = new RegisterRequest();
        lenient().when(userService.saveUser(registerRequest)).thenReturn(ResponseObject.builder()
                .status("SUCCESS").message("Create user successfully!").build());

        ResponseEntity<ResponseObject> responseEntity = adminController.createUser(mockRequest, registerRequest);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void createUser_shouldReturnInternalServerErrorResponseWhenCatchUserException() throws JSONException, UserException {

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(true);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");
        lenient().when(userService.getAllUser()).thenThrow(new UserException("Error getting users"));
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        RegisterRequest registerRequest = new RegisterRequest();
        lenient().when(userService.saveUser(registerRequest)).thenThrow(new UserException("Error saving user"));

        ResponseEntity<ResponseObject> response = adminController.createUser(mockRequest, registerRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR", response.getBody().getStatus());
        assertEquals("Error saving user", response.getBody().getMessage());
    }

    //Finish testing createUser() function
    @Test
    void createUser_shouldReturnSuccess() throws UserException {

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(true);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        RegisterRequest registerRequest = new RegisterRequest();
        when(userService.saveUser(registerRequest)).thenReturn(ResponseObject.builder()
                .status("SUCCESS").message("Create user successfully!").build());

        ResponseEntity<ResponseObject> responseEntity = adminController.createUser(mockRequest, registerRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("SUCCESS", responseEntity.getBody().getStatus());
        assertEquals("Create user successfully!", responseEntity.getBody().getMessage());
    }

    @Test
    public void getDetailApplication_shouldReturnSuccess() throws Exception {

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
        ResponseEntity<ResponseObject> responseEntity = adminController.getDetailApplication(1, mockRequest);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseEntity.getBody().getStatus());
        assertEquals("Application", responseEntity.getBody().getMessage());
    }
    @Test
    public void getDetailApplication_shouldReturnErrorWhenApplicationNotFound() throws Exception {

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
        ResponseEntity<ResponseObject> responseEntity = adminController.getDetailApplication(1, mockRequest);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("ERROR", responseObject.getStatus());
        assertEquals("Application not found", responseObject.getMessage());
    }
    @Test
    public void getDetailApplications_shouldReturnForbiddenWhenUserNotAdmin() throws Exception {

        // Mock user
        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(false);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(false);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");
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
        ResponseEntity<ResponseObject> responseEntity = adminController.getDetailApplication(1, mockRequest);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
    @Test
    public void getApplications_shouldReturnSuccessWithApplicationList() throws Exception {

        // Mock user
        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(true);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");
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
    public void getApplications_shouldReturnForbiddenWhenUserNotAdmin() throws Exception {

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(false);
        lenient().when(mockUserInfo.getUserId()).thenReturn(6);
        lenient().when(userService.isEnabled(2)).thenReturn(false);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");
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
        // Assert
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
    @Test
    void deletePosition_shouldReturnSuccessWhenPositionDeletedSuccessfully() throws JSONException, UserException, PositionException {


        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(true);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        Position mockPosition = new Position();
        mockPosition.setId(1);
        mockPosition.setTitle("Front-end Dev");
        mockPosition.setJobDescription("abc");
        mockPosition.setJobRequirements("abc");
        mockPosition.setSalary(BigDecimal.valueOf(1000.00));
        mockPosition.setPostDate(Date.valueOf("2023-10-26"));
        mockPosition.setExpireDate(Date.valueOf("2023-10-26"));
        mockPosition.setUpdateDate(null);
        mockPosition.setLocation("fpt");

         // Assuming positionID is 1
        when(positionService.getSelectedPosition(1)).thenReturn(mockPosition);
        doNothing().when(positionService).deletePosition(1); // Mocking delete success
        // Act
        ResponseEntity<ResponseObject> responseEntity = adminController.deletePosition(1, mockRequest);
        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseObject.getStatus());
        assertEquals("Position deleted successfully", responseObject.getMessage());
    }
    @Test
    void deletePosition_shouldReturnErrorWhenPositionExceptionThrown() throws JSONException, UserException, PositionException {

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(true);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        Position mockPosition = new Position();
        mockPosition.setId(1);
        mockPosition.setTitle("Front-end Dev");
        mockPosition.setJobDescription("abc");
        mockPosition.setJobRequirements("abc");
        mockPosition.setSalary(BigDecimal.valueOf(1000.00));
        mockPosition.setPostDate(Date.valueOf("2023-10-26"));
        mockPosition.setExpireDate(Date.valueOf("2023-10-26"));
        mockPosition.setUpdateDate(null);
        mockPosition.setLocation("fpt");

        when(positionService.getSelectedPosition(1)).thenReturn(mockPosition);
        doThrow(new PositionException("Simulated exception")).when(positionService).deletePosition(1);

        ResponseEntity<ResponseObject> responseEntity = adminController.deletePosition(1, mockRequest);
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("ERROR", responseObject.getStatus());
        assertEquals("Error deleting position: Simulated exception", responseObject.getMessage());
    }
    @Test
    void deletePosition_shouldReturnBadRequestWhenAccountNotActive() throws JSONException, UserException, PositionException {


        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(false);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(false);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        ResponseEntity<ResponseObject> responseEntity = adminController.deletePosition(1, mockRequest);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("ERROR", responseObject.getStatus());
        assertEquals("Account not active!", responseObject.getMessage());
    }
    @Test
    void editPosition_shouldReturnBadRequestWhenAccountNotActive() throws JSONException, UserException, PositionException {

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(false);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(false);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        Position poss = new Position();
        poss.setId(2);
        poss.setTitle("Back-end Dev");
        poss.setJobDescription("bcd");
        poss.setJobRequirements("bcd");
        poss.setSalary(BigDecimal.valueOf(2000.00));
        poss.setPostDate(Date.valueOf("2023-10-25"));
        poss.setExpireDate(Date.valueOf("2023-10-25"));
        poss.setUpdateDate(null);
        poss.setLocation("fpt");

        // Create a PositionRequest object directly in the test method
        PositionRequest positionRequest = new PositionRequest();
        positionRequest.setTitle(poss.getTitle());
        positionRequest.setJobDescription(poss.getJobDescription());
        positionRequest.setJobRequirements(poss.getJobRequirements());
        positionRequest.setSalary(poss.getSalary());
        positionRequest.setExpireDate(poss.getExpireDate());
        positionRequest.setLocation(poss.getLocation());

        // Add more attributes if needed

        // Act
        ResponseEntity<ResponseObject> responseEntity = adminController.editPosition(1, positionRequest, mockRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("ERROR", responseObject.getStatus());
        assertEquals("Account not active!", responseObject.getMessage());
    }
    @Test
    void editPosition_shouldReturnSuccessWhenPositionEditedSuccessfully() throws JSONException, UserException, PositionException {

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(true);
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        PositionRequest positionRequest = new PositionRequest();
        positionRequest.setTitle("Software Engineer");
        positionRequest.setJobDescription("Build web applications");
        positionRequest.setJobRequirements("3 years of experience");
        positionRequest.setSalary(BigDecimal.valueOf(5000.00));
        positionRequest.setExpireDate(Date.valueOf("2023-11-30"));
        positionRequest.setLocation("FPT");

        Position existingPosition = new Position();
        existingPosition.setTitle("Software Engineer");
        existingPosition.setJobDescription("Build web applications ");
        existingPosition.setJobRequirements("1 years of experience");
        existingPosition.setSalary(BigDecimal.valueOf(2000.00));
        existingPosition.setExpireDate(Date.valueOf("2023-10-30"));
        existingPosition.setLocation("FPT");

        // Mocking behaviors
        when(positionService.getSelectedPosition(1)).thenReturn(existingPosition);
        // Act
        ResponseEntity<ResponseObject> responseEntity = adminController.editPosition(1, positionRequest, mockRequest);
        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseObject.getStatus());
        assertEquals("Position updated successfully", responseObject.getMessage());

        // Verify that the editPosition method is called once with the updated position
        verify(positionService, times(1)).editPosition(eq(1), any(Position.class));
    }
    @Test
    void listInterviewsession() throws UserException, JSONException, InterviewSessionException {
        User mockUser = new User();
        mockUser.setId(5);
        mockUser.setUsername("thuytrang");
        mockUser.setPassword("$2a$10$HLVCKSnNE1iUAo8zLh0ioe9CkF3n1L8HM6HNZawxvW2cFswUt9Vlu");
        mockUser.setEmail("trang@gmail.com");
        mockUser.setPhoneNumber("0384928392");
        mockUser.setRoleName(Role.Admin);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf("2023-11-10"));
        mockUser.setUpdateDate(Date.valueOf("2023-11-10"));

        var jwtToken = jwtService.generateToken(mockUser);

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(5);
        lenient().when(userService.isEnabled(5)).thenReturn(true);
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        List<InterviewSession> mockInterviewSessions = new ArrayList<>();
        InterviewSession interviewSession = new InterviewSession();
        interviewSession.setId(1);
        interviewSession.setInterviewerID(null);
        interviewSession.setApplicationID(1);
//        interviewSession.setPositionID(null);
//        interviewSession.setCandidateID(null);
        interviewSession.setDate(Date.valueOf("2023-10-29"));
        interviewSession.setLocation("fpt");
        interviewSession.setStatus(SessionStatus.valueOf("NotOnSchedule"));
        interviewSession.setLocation("NotYet");
        interviewSession.setNotes("abc");
        mockInterviewSessions.add(interviewSession);


        interviewSession.setId(2);
        interviewSession.setInterviewerID(3);
        interviewSession.setApplicationID(1);
//        interviewSession.setPositionID(null);
//        interviewSession.setCandidateID(null);
        interviewSession.setDate(Date.valueOf("2023-10-25"));
        interviewSession.setLocation("fpt");
        interviewSession.setStatus(SessionStatus.valueOf("Yet"));
        interviewSession.setLocation("Good");
        interviewSession.setNotes("bcd");
        mockInterviewSessions.add(interviewSession);


        ResponseEntity<ResponseObject> responseEntity = adminController.listInterviewsession(mockRequest);
//
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseObject.getStatus());
        assertEquals("Loading position successfully", responseObject.getMessage());

        List<InterviewSession> returnedData = (List<InterviewSession>) responseObject.getData();
        assertNotNull(returnedData);

        verify(interviewSessionService, times(1)).getAllInterviewSession();

    }

    @Test
    void getInterviewSessionId() throws UserException, InterviewSessionException {
        User mockUser = new User();
        mockUser.setId(2);
        mockUser.setUsername("bcd");
        mockUser.setPassword("$2a$10$SgZX47bsE057V9z4n1NeG.y0hJkv1scG07pmPjPmBovIAnw4RhB7y");
        mockUser.setEmail("b123@gmail.com");
        mockUser.setPhoneNumber("0988888888");
        mockUser.setRoleName(Role.Admin);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf("2023-10-23"));
        mockUser.setUpdateDate(Date.valueOf("2023-10-23"));

        var jwtToken = jwtService.generateToken(mockUser);

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(2);
        lenient().when(userService.isEnabled(2)).thenReturn(true);
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        InterviewSession mockinterviewSession = new InterviewSession();
        mockinterviewSession.setId(1);
        mockinterviewSession.setInterviewerID(null);
        mockinterviewSession.setApplicationID(1);
//        mockinterviewSession.setPositionID(null);
//        mockinterviewSession.setCandidateID(null);
        mockinterviewSession.setDate(Date.valueOf("2023-10-29"));
        mockinterviewSession.setLocation("fpt");
        mockinterviewSession.setStatus(SessionStatus.valueOf("NotOnSchedule"));
        mockinterviewSession.setLocation("NotYet");
        mockinterviewSession.setNotes("abc");

        when(interviewSessionService.getSelectedInterviewSession(1)).thenReturn(mockinterviewSession);
        ResponseEntity<ResponseObject> responseEntity = adminController.detailInterviewSessionId(1,mockRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseObject.getStatus());
        assertEquals("Loading detailinterviewsession successfully", responseObject.getMessage());

        InterviewSession returnedInterviewsession = (InterviewSession) responseObject.getData();
        assertNotNull(returnedInterviewsession);
        assertEquals(1, returnedInterviewsession.getId());
        assertEquals("NotOnSchedule", returnedInterviewsession.getStatus());


    }

    @Test
    void getEvaluate() {

    }
}



