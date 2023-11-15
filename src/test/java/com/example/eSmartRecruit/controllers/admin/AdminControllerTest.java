package com.example.eSmartRecruit.controllers.admin;
import com.example.eSmartRecruit.authentication.request_reponse.RegisterRequest;
import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.config.JwtService;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.controllers.request_reponse.request.ApplicationResultRequest;
import com.example.eSmartRecruit.controllers.request_reponse.request.InterviewSessionRequest;
import com.example.eSmartRecruit.exception.ApplicationException;
import com.example.eSmartRecruit.exception.InterviewSessionException;
import com.example.eSmartRecruit.exception.PositionException;
import com.example.eSmartRecruit.exception.UserException;
import com.example.eSmartRecruit.models.*;
import com.example.eSmartRecruit.models.enumModel.*;
import com.example.eSmartRecruit.services.IStorageService;
import com.example.eSmartRecruit.services.impl.*;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
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
    private PositionService positionService;

    @Mock
    InterviewSessionService interviewSessionService;

    @Mock
    ReportService reportService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }
    @Test
    void PositionAdmin() throws UserException, PositionException {
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
//
        //mock user
        User mockUser = new User();
        mockUser.setId(5);
        mockUser.setUsername("admin");
        mockUser.setPassword("$10$/nCR/hYK8RJFwvHCxCwBQOirAhm9jdcxaSSKCBFJCgCLimHFTWUuy");
        mockUser.setEmail("admin1@gmail.com");
        mockUser.setPhoneNumber("987654321");
        mockUser.setRoleName(Role.Admin);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf("2023-10-31"));
        mockUser.setUpdateDate(Date.valueOf("2023-10-31"));

        var jwtToken = jwtService.generateToken(mockUser);
        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(5);
        lenient().when(userService.isEnabled(5)).thenReturn(true);
        lenient().when(userService.getUserRole(5)).thenReturn("Admin");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        ResponseEntity<ResponseObject> responseEntity = adminController.PositionAdmin(mockRequest);
//
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseObject.getStatus());
        assertEquals("Loading position successfully", responseObject.getMessage());

        List<Position> returnedData = (List<Position>) responseObject.getData();
        assertNotNull(returnedData);

        verify(positionService, times(1)).getAllPosition();
    }
    @Test
    void getDetailPositionAdmin() throws PositionException,  UserException{
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

        //mock user
        User mockUser = new User();
        mockUser.setId(5);
        mockUser.setUsername("admin");
        mockUser.setPassword("$10$/nCR/hYK8RJFwvHCxCwBQOirAhm9jdcxaSSKCBFJCgCLimHFTWUuy");
        mockUser.setEmail("admin1@gmail.com");
        mockUser.setPhoneNumber("987654321");
        mockUser.setRoleName(Role.Admin);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf("2023-10-31"));
        mockUser.setUpdateDate(Date.valueOf("2023-10-31"));

        var jwtToken = jwtService.generateToken(mockUser);
        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(5);
        lenient().when(userService.isEnabled(5)).thenReturn(true);
        lenient().when(userService.getUserRole(5)).thenReturn("Admin");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);


        when(positionService.getSelectedPosition(1)).thenReturn(mockPosition);
        ResponseEntity<ResponseObject> responseEntity = adminController.getDetailPositionAdmin(1,mockRequest);

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
    @Test
    void home() throws UserException, JSONException {
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

        // Giả lập ExtractUser

        // Giả lập các giá trị trả về từ các service
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
    //Start testing getUsers() function
    @Test
    void getUsers_shouldReturnSuccessWithUserList() throws JSONException, UserException {
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
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        List<User> mockUserList = new ArrayList<>();
        mockUserList.add(User.builder().id(3)
                .username("khang").password("khang123")
                .email("khang123@gmail.com").phoneNumber(null)
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

        var jwtToken = jwtService.generateToken(mockUser);

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
        User mockUser = new User();
        mockUser.setId(2);
        mockUser.setUsername("bcd");
        mockUser.setPassword("$2a$10$SgZX47bsE057V9z4n1NeG.y0hJkv1scG07pmPjPmBovIAnw4RhB7y");
        mockUser.setEmail("b123@gmail.com");
        mockUser.setPhoneNumber("0988888888");
        mockUser.setRoleName(Role.Candidate);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf("2023-10-23"));
        mockUser.setUpdateDate(Date.valueOf("2023-10-23"));

        var jwtToken = jwtService.generateToken(mockUser);

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
        lenient().when(userService.getUserRole(2)).thenReturn("Admin");
        when(userService.getAllUser()).thenThrow(new UserException("Error getting users"));
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        ResponseEntity<ResponseObject> response = adminController.getUsers(mockRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR", response.getBody().getStatus());
    }
    //Finish testing getUser() function

    //Start testing createUser() function
    @Test
    void createUser_shouldReturnSuccess() throws UserException {
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
    void createUser_shouldReturnForbiddenResponseWhenUserNotEnabled() throws UserException {
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

        var jwtToken = jwtService.generateToken(mockUser);

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
        User mockUser = new User();
        mockUser.setId(2);
        mockUser.setUsername("bcd");
        mockUser.setPassword("$2a$10$SgZX47bsE057V9z4n1NeG.y0hJkv1scG07pmPjPmBovIAnw4RhB7y");
        mockUser.setEmail("b123@gmail.com");
        mockUser.setPhoneNumber("0988888888");
        mockUser.setRoleName(Role.Candidate);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf("2023-10-23"));
        mockUser.setUpdateDate(Date.valueOf("2023-10-23"));

        var jwtToken = jwtService.generateToken(mockUser);

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
    void scheduleInterview_shouldReturnSuccess() throws InterviewSessionException {

        Integer sessionId = 1;

        InterviewSession interviewSession = new InterviewSession();
        interviewSession.setId(sessionId);
        interviewSession.setInterviewerID(1);
        interviewSession.setApplicationID(1);
        interviewSession.setDate(Date.valueOf("2023-11-11"));
        interviewSession.setLocation("FPT");
        interviewSession.setStatus(SessionStatus.Yet);
        interviewSession.setResult(SessionResult.NotYet);
        interviewSession.setNotes("Nothing");

        InterviewSession newInterviewSession = new InterviewSession();
        newInterviewSession.setId(sessionId);
        newInterviewSession.setInterviewerID(1);
        newInterviewSession.setApplicationID(1);
        newInterviewSession.setDate(Date.valueOf("2023-11-20"));
        newInterviewSession.setLocation("FPT");
        newInterviewSession.setStatus(SessionStatus.Yet);
        newInterviewSession.setResult(SessionResult.Good);
        newInterviewSession.setNotes("Nothing");


        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        InterviewSessionRequest interviewSessionRequest = new InterviewSessionRequest();
        interviewSessionRequest.setInterviewerId(1);
        interviewSessionRequest.setDate(Date.valueOf("2023-11-20"));
        interviewSessionRequest.setLocation("FPT");
        interviewSessionRequest.setNotes("Nothing");
        lenient().when(interviewSessionService.scheduleInterview(sessionId,interviewSessionRequest)).thenReturn(newInterviewSession);

        ResponseEntity<ResponseObject> response = adminController.scheduleInterview(sessionId,mockRequest, interviewSessionRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SUCCESS", response.getBody().getStatus());
        assertEquals("Schedule successfully!", response.getBody().getMessage());
    }

    @Test
    void getReport_shouldReturnSuccess() throws ApplicationException {
        Integer sessionId = 1;
        Report report = new Report();
        report.setId(1);
        report.setSessionID(1);
        report.setReportName("Haha");
        report.setReportData("Ha");
        report.setCreateDate(Date.valueOf("2023-11-1"));
        report.setUpdateDate(Date.valueOf("2023-11-1"));
        lenient().when(reportService.getReportBySessionId(1)).thenReturn(report);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", report.getId());
        data.put("report_name", report.getReportName());
        data.put("report_data", report.getReportData());
        data.put("createDate", report.getCreateDate() != null ? report.getCreateDate().toString() : null);
        data.put("updateDate", report.getUpdateDate() != null ? report.getUpdateDate().toString() : null);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        ResponseEntity<ResponseObject> response = adminController.getReport(sessionId,mockRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SUCCESS", response.getBody().getStatus());
        assertEquals("Report", response.getBody().getMessage());
        assertEquals(data,response.getBody().getData());
    }

    // start testing getCandidateInformation
    @Test
    void getCandidateInformation_shouldReturnSuccess() throws ApplicationException, UserException, JSONException {
        User mockUser = new User();
        mockUser.setId(2);
        mockUser.setUsername("bcd");
        mockUser.setPassword("$2a$10$SgZX47bsE057V9z4n1NeG.y0hJkv1scG07pmPjPmBovIAnw4RhB7y");
        mockUser.setEmail("b123@gmail.com");
        mockUser.setPhoneNumber("0988888888");
        mockUser.setRoleName(Role.Candidate);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf("2023-10-23"));
        mockUser.setUpdateDate(Date.valueOf("2023-10-23"));
        lenient().when(userService.getUserById(2)).thenReturn(mockUser);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("username", mockUser.getUsername());
        data.put("email", mockUser.getEmail());
        data.put("phonenumber", mockUser.getPhoneNumber());
        data.put("roleName", mockUser.getRoleName().name());

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        ResponseEntity<ResponseObject> response = adminController.getCandidateInformation(2, mockRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals(data,response.getBody().getData());
    }

    @Test
    void getCandidateInformation_shouldReturnError() throws ApplicationException, UserException, JSONException {
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
        lenient().when(userService.getUserById(2)).thenReturn(mockUser);
        //lenient().when(mockUser.getRoleName().equals(Role.Candidate)).thenReturn(false);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("username", mockUser.getUsername());
        data.put("email", mockUser.getEmail());
        data.put("phonenumber", mockUser.getPhoneNumber());
        data.put("roleName", mockUser.getRoleName().name());

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        ResponseEntity<ResponseObject> response = adminController.getCandidateInformation(2, mockRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Not a candidate",response.getBody().getMessage());
    }
    // end testing getCandidateInformation


    //start testing updateApplicationStatus
    @Test
    void updateApplicationStatus_shouldReturnSuccess() throws UserException, PositionException {
        User mockUser = new User();

        ApplicationResultRequest resultRequest = new ApplicationResultRequest();
        resultRequest.setStatus(ApplicationStatus.Pending.name());

        Integer applicationId = 1;
        Application application = new Application();
        application.setId(applicationId);
        application.setCandidateID(1);
        application.setPositionID(1);
        application.setCv("cv");
        application.setCreateDate(Date.valueOf("2023-11-11"));
        application.setUpdateDate(Date.valueOf("2023-11-11"));

        lenient().when(applicationService.findById(1)).thenReturn(application);
        Position mockPosition = new Position();
        mockPosition.setTitle("Bao ve");
        lenient().when(positionService.getSelectedPosition(application.getPositionID())).thenReturn(mockPosition);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("applicationID", application.getId());
        data.put("positionTitle", positionService.getSelectedPosition(application.getPositionID()).getTitle());
        data.put("status", application.getStatus());

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        ResponseEntity<ResponseObject> response = adminController.updateApplicationStatus(1, mockRequest,resultRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SUCCESS", response.getBody().getStatus());
        assertEquals(data,response.getBody().getData());
    }

    @Test
    void updateApplicationStatus_shouldReturnErrorInvalidStatus() throws UserException, PositionException {
        User mockUser = new User();

        ApplicationResultRequest resultRequest = new ApplicationResultRequest();
        resultRequest.setStatus("Hello");

        Integer applicationId = 1;
        Application application = new Application();
        application.setId(applicationId);
        application.setCandidateID(1);
        application.setPositionID(1);
        application.setCv("cv");
        application.setCreateDate(Date.valueOf("2023-11-11"));
        application.setUpdateDate(Date.valueOf("2023-11-11"));

        lenient().when(applicationService.findById(1)).thenReturn(application);
        Position mockPosition = new Position();
        mockPosition.setTitle("Bao ve");
        lenient().when(positionService.getSelectedPosition(application.getPositionID())).thenReturn(mockPosition);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("applicationID", application.getId());
        data.put("positionTitle", positionService.getSelectedPosition(application.getPositionID()).getTitle());
        data.put("status", application.getStatus());

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        ResponseEntity<ResponseObject> response = adminController.updateApplicationStatus(1, mockRequest,resultRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("ERROR", response.getBody().getStatus());
        //assertEquals(data,response.getBody().getData());
    }



    //end testing updateApplicationStatus

}



