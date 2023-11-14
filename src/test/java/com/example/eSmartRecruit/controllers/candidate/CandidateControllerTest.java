package com.example.eSmartRecruit.controllers.candidate;

import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.config.JwtService;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.controllers.request_reponse.request.UserRequest;
import com.example.eSmartRecruit.exception.ApplicationException;
import com.example.eSmartRecruit.exception.FileUploadException;
import com.example.eSmartRecruit.exception.PositionException;
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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;

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
    private IStorageService storageService;

    @Mock
    private ApplicationService applicationService;

    private JwtService jwtService;

    // Biến instance để lưu trữ
    private MockMultipartFile mockFile;
    private User mockUser;
    private Position position1;
    private Position position2;
    private Application mockApp1;
    private Application mockApp2;
    private List<Position> mockPositions;
    private HttpServletRequest mockRequest;
    private List<Application> mockApplications;

    @BeforeEach
    void setUp() throws Exception {
        // file đúng chuẩn pdf
        mockFile = new MockMultipartFile("cv", "cv.pdf", "application/pdf", "cv data".getBytes());

        // Setup candidate đăng nhập
        mockUser = new User();
        mockUser.setId(4);
        mockUser.setUsername("khang");
        mockUser.setPassword("$2a$10$S5x1eUGgsbXA4RJfrnc07ueCheYAVNMXsqw23/HfivFQJsaowrTXW");
        mockUser.setEmail("khang123@gmail.com");
        mockUser.setPhoneNumber("0999999999");
        mockUser.setRoleName(Role.Candidate);
        mockUser.setStatus(UserStatus.Active);
        mockUser.setCreateDate(Date.valueOf("2023-11-02"));
        mockUser.setUpdateDate(Date.valueOf("2023-11-02"));

        jwtService = new JwtService();
        var jwtToken = jwtService.generateToken(mockUser);

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(4);
        lenient().when(userService.isEnabled(4)).thenReturn(true);
        lenient().when(userService.getUserById(4)).thenReturn(mockUser);
        mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);


        // setup list position
        mockPositions = new ArrayList<>();

        position1 = new Position();
        position1.setId(1);
        position1.setTitle("Software Engineer");
        position1.setJobDescription("Build web applications");
        position1.setJobRequirements("3 years of experience");
        position1.setSalary(BigDecimal.valueOf(5000));
        position1.setPostDate(Date.valueOf("2023-10-10"));
        position1.setExpireDate(Date.valueOf("2023-10-30"));
        position1.setUpdateDate(Date.valueOf("2023-10-10"));
        position1.setLocation("FPT, Thu Duc City");
        mockPositions.add(position1);

        position2 = new Position();
        position2.setId(2);
        position2.setTitle("Security Engineer");
        position2.setJobDescription("Responsible for web application security");
        position2.setJobRequirements("3 years of experience");
        position2.setSalary(BigDecimal.valueOf(5000));
        position2.setPostDate(Date.valueOf("2023-10-10"));
        position2.setExpireDate(Date.valueOf("2023-10-30"));
        position2.setUpdateDate(Date.valueOf("2023-10-10"));
        position2.setLocation("FPT, Thu Duc City");
        mockPositions.add(position2);

        //setup applications
        mockApplications = new ArrayList<>();
        mockApp1 = new Application();
        mockApp1.setId(1);
        mockApp1.setCandidateID(4);
        mockApp1.setPositionID(1);
        mockApp1.setStatus(ApplicationStatus.Pending);
        mockApp1.setCv("application1.pdf");
        mockApp1.setCreateDate(Date.valueOf("2023-11-5"));
        mockApp1.setUpdateDate(Date.valueOf("2023-11-5"));
        mockApplications.add(mockApp1);

        mockApp2 = new Application();
        mockApp2.setId(1);
        mockApp2.setCandidateID(4);
        mockApp2.setPositionID(2);
        mockApp2.setStatus(ApplicationStatus.Pending);
        mockApp2.setCv("application2.pdf");
        mockApp2.setCreateDate(Date.valueOf("2023-11-5"));
        mockApp2.setUpdateDate(Date.valueOf("2023-11-5"));
        mockApplications.add(mockApp2);
    }


    User mockUser1;
    ExtractUser mockUserInfo1;
    JwtService jwtService1 = new JwtService();
    HttpServletRequest mockRequest1;
    // mock đối tượng user không tồn tại
    void UserNotEnabled() throws Exception{
        // Mock user
        mockUser1 = new User();
        mockUser1.setId(6);
        mockUserInfo1 = mock(ExtractUser.class);
        lenient().when(mockUserInfo1.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo1.getUserId()).thenReturn(6);
        lenient().when(userService.isEnabled(8)).thenReturn(true);// tìm 8 thay v 6

        jwtService1 = new JwtService();
        var jwtToken1 = jwtService1.generateToken(mockUser1);
        mockRequest1 = mock(HttpServletRequest.class);
        lenient().when(mockRequest1.getHeader("Authorization")).thenReturn("Bearer " + jwtToken1);

    }

    @Test
    void home_success() throws Exception {
        // Mocking positionService để trả về mockPositions
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
    void home_internalServerError() throws Exception {
        // Mock hành vi của positionService.getAllPosition() để ném ra một exception
        when(positionService.getAllPosition()).thenThrow(new RuntimeException("Simulated internal server error"));

        // Gọi API
        ResponseEntity<ResponseObject> responseEntity = candidateController.home();

        // Xác nhận phản hồi
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        ResponseObject responseObject = responseEntity.getBody();
        assertEquals("ERROR", responseObject.getStatus());
        assertEquals("Simulated internal server error", responseObject.getMessage());
        // Đảm bảo rằng data là null hoặc không có trong trường hợp lỗi
        assertEquals(null, responseObject.getData());

        // Xác nhận rằng positionService.getAllPosition() được gọi một lần
        verify(positionService, times(1)).getAllPosition();
    }

    ///////////////
    @Test
    void getDetailPosition_success() throws Exception {
        when(positionService.getSelectedPosition(1)).thenReturn(position1);
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

    @Test
    void getDetailPosition_notFound() throws Exception {
        // chỉ có id 1,2
        when(positionService.getSelectedPosition(anyInt())).thenReturn(null);
        ResponseEntity<ResponseObject> responseEntity = candidateController.getDetailPosition(1);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertEquals("ERROR", responseObject.getStatus());
        assertEquals("Position not found", responseObject.getMessage());
        assertEquals(null, responseObject.getData());

        verify(positionService, times(1)).getSelectedPosition(1);
    }

    @Test
    void getDetailPosition_internalServerError() throws Exception {
        // Mock hành vi của positionService.getSelectedPosition() để ném ra một exception
        when(positionService.getSelectedPosition(anyInt())).thenThrow(new RuntimeException("Simulated internal server error"));

        // gọi api
        ResponseEntity<ResponseObject> responseEntity = candidateController.getDetailPosition(1);

        // xác nhận phản hồi
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("ERROR", responseObject.getStatus());
        assertEquals("Simulated internal server error", responseObject.getMessage());
        // xác nhận body null
        assertEquals(null, responseObject.getData());

        // kiểm tra chắc chắc hàm getSelectedPosition được gọi 1 lần
        verify(positionService, times(1)).getSelectedPosition(1);
    }


    /////////////////////////
    @Test
    void applyForPosition_success() throws Exception {
        lenient().when(storageService.storeFile(mockFile)).thenReturn("generatedFileName");
        lenient().when(positionService.isPresent(1)).thenReturn(true);
        lenient().when(applicationService.apply(any(Application.class))).thenReturn("Successfully applied!");

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
    void applyForPosition_badRequest_whenUserNotEnabled() throws Exception {
        UserNotEnabled();

        // Act
        ResponseEntity<ResponseObject> responseEntity = candidateController.applyForPosition(1, mockRequest1, mockFile);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("ERROR", responseObject.getStatus());
        assertEquals("Account not active!", responseObject.getMessage());
    }

    @Test
    void applyForPosition_badRequest_whenCVNotEnabled() throws Exception {
        // Act
        ResponseEntity<ResponseObject> responseEntity = candidateController.applyForPosition(1, mockRequest, null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("ERROR", responseObject.getStatus());
        assertEquals("CV is required!", responseObject.getMessage());
    }

    @Test
    void applyForPosition_badRequest_whenPositionNotOpen() throws Exception {
        // mock positon - false
        when(positionService.isPresent(1)).thenReturn(false);

        // Act
        ResponseEntity<ResponseObject> responseEntity = candidateController.applyForPosition(1, mockRequest, mockFile);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("ERROR", responseObject.getStatus());
        assertEquals("Position not open!", responseObject.getMessage());
    }

    @Test
    void applyForPositionErrorNotPDF() throws Exception {
        // file không phải là pdf
        MockMultipartFile mockFile = new MockMultipartFile("cv", "cv.docx", "application/pdf", "cv data".getBytes());

        lenient().when(storageService.isPDF(mockFile)).thenReturn(false);
        when(storageService.storeFile(mockFile)).thenThrow(new FileUploadException("Only pdf file accepted!"));
        lenient().when(positionService.isPresent(1)).thenReturn(true);
        lenient().when(applicationService.apply(any(Application.class))).thenReturn("Only pdf file accepted!");

        ResponseEntity<ResponseObject> responseEntity = candidateController.applyForPosition(1, mockRequest, mockFile);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("ERROR", responseObject.getStatus());
        assertEquals("Only pdf file accepted!", responseObject.getMessage());
    }

    //////////////////////////////////
    @Test
    void getMyApplications_success() throws Exception {

        when(userService.getUserById(4)).thenReturn(mockUser);
        when(applicationService.getApplicationsByCandidateId(4)).thenReturn(mockApplications);
        when(positionService.getSelectedPosition(2)).thenReturn(position2);
        lenient().when(positionService.isPresent(2)).thenReturn(true);
        when(positionService.getSelectedPosition(1)).thenReturn(position1);
        when(positionService.getSelectedPosition(2)).thenReturn(position2);

        ResponseEntity<ResponseObject> responseEntity = candidateController.getMyApplications(mockRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseObject.getStatus());

        List<Map<String, Object>> applicationList = (List<Map<String, Object>>) responseObject.getData();
        assertNotNull(applicationList);
        assertEquals(2, applicationList.size());

        Map<String, Object> applicationMap = applicationList.get(0);
        assertNotNull(applicationMap);
        assertEquals(1, applicationMap.get("applicationID"));
        assertEquals("Software Engineer", applicationMap.get("positionTitle"));
        assertEquals(ApplicationStatus.Pending, applicationMap.get("status"));
        assertNotNull(applicationMap.get("applicationDate"));
    }

    @Test
    void getMyApplications_whenUserNotEnabled() throws Exception {
        UserNotEnabled();

        ResponseEntity<ResponseObject> responseEntity = candidateController.getMyApplications(mockRequest1);//
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("ERROR", responseObject.getStatus());
        assertEquals("Account not active!", responseObject.getMessage());
    }

    @Test
    void getMyApplications_notFound() throws Exception {

        when(userService.getUserById(4)).thenReturn(mockUser);
        when(applicationService.getApplicationsByCandidateId(anyInt())).thenReturn(Collections.emptyList());

        ResponseEntity<ResponseObject> responseEntity = candidateController.getMyApplications(mockRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseObject.getStatus());
        assertEquals("No applications found.", responseObject.getMessage());
        assertEquals(Collections.emptyList(), responseEntity.getBody().getData());
    }

    @Test
    void getMyApplications_internalServerError() throws Exception {

        when(userService.getUserById(4)).thenReturn(mockUser);
        when(applicationService.getApplicationsByCandidateId(anyInt())).thenThrow(new RuntimeException("Some internal server"));

        ResponseEntity<ResponseObject> responseEntity = candidateController.getMyApplications(mockRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        assertEquals("ERROR", responseObject.getStatus());
        assertEquals("Some internal server", responseObject.getMessage());
        assertEquals(responseObject.getData(), null);
    }

    ////////////////
    @Test
    void getApplicationDetails_success() throws Exception {

        when(applicationService.getApplicationById(1)).thenReturn(mockApp1);
        ResponseEntity<ResponseObject> responseEntity = candidateController.getApplicationDetails(1, mockRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseObject.getStatus());
    }
    ////////////////
    @Test
    void updateApplication() throws Exception {
        lenient().when(storageService.storeFile(mockFile)).thenReturn("generatedFileName.pdf");
        lenient().when(positionService.isPresent(1)).thenReturn(true);

        var newApplication = new Application("generatedFileName.pdf");

        ApplicationRepos repos = mock(ApplicationRepos.class);
        lenient().when(repos.findById(mockApp1.getId())).thenReturn(Optional.of(mockApp1));

        //when(applicationRepository.findById(1)).thenReturn(Optional.of(mockApplication));
        lenient().when(applicationService.update(4, newApplication, 1)).thenReturn("update Success");

        ResponseEntity<ResponseObject> responseEntity = candidateController.updateApplyPosition(1, mockRequest, mockFile);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        System.out.println(applicationService.update(4, newApplication, 1));
        System.out.println(responseEntity);
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseObject.getStatus());
        assertEquals("update Success", responseObject.getMessage());
    }

    @Test
    void deleteApplication() throws Exception {

        lenient().when(storageService.storeFile(mockFile)).thenReturn("generatedFileName.pdf");
        lenient().when(positionService.isPresent(1)).thenReturn(true);

        var appId = 1;

        ApplicationRepos repos = mock(ApplicationRepos.class);
        lenient().when(repos.findById(appId)).thenReturn(Optional.of(mockApp1));
        lenient().when(applicationService.deletejob(4, 1)).thenReturn("Successfully deleted!");

        ResponseEntity<ResponseObject> responseEntity = candidateController.deleteApplyPosition(1, mockRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        System.out.println(applicationService.deletejob(4, 1));
        System.out.println(responseEntity);
        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseObject.getStatus());
        assertEquals("Successfully deleted!", responseObject.getMessage());
    }

    @Test
    void editProfile() throws Exception {

        User updateMockUser = new User();
        updateMockUser.setId(4);
        updateMockUser.setUsername("khang");
        updateMockUser.setPassword("$2a$10$S5x1eUGgsbXA4RJfrnc07ueCheYAVNMXsqw23/HfivFQJsaowrTXW");
        updateMockUser.setEmail("khangupdate@gmail.com");// thay đổi mai
        updateMockUser.setPhoneNumber("0999999999");
        updateMockUser.setRoleName(Role.Candidate);
        updateMockUser.setStatus(UserStatus.Active);
        updateMockUser.setCreateDate(Date.valueOf("2023-11-02"));
        updateMockUser.setUpdateDate(Date.valueOf("2023-11-02"));

        var jwtToken = jwtService.generateToken(mockUser);

        ExtractUser mockUserInfo = mock(ExtractUser.class);
        lenient().when(mockUserInfo.isEnabled()).thenReturn(true);
        lenient().when(mockUserInfo.getUserId()).thenReturn(4);
        lenient().when(userService.isEnabled(4)).thenReturn(true);
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        lenient().when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        lenient().when(storageService.storeFile(mockFile)).thenReturn("generatedFileName");
        lenient().when(positionService.isPresent(1)).thenReturn(true);
        lenient().when(applicationService.apply(any(Application.class))).thenReturn("Successfully applied!");

        var userId = mockUserInfo.getUserId();
        var userRequest = new UserRequest("khang@gmail.com", "0999999999");
        lenient().when(userService.updateUser(userRequest, userId)).thenReturn(updateMockUser);
        ResponseEntity<ResponseObject> responseEntity = candidateController.updateUser(mockRequest, userRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("email", updateMockUser.getEmail());
        data.put("phoneNumber", updateMockUser.getPhoneNumber());

        ResponseObject responseObject = responseEntity.getBody();
        assertNotNull(responseObject);
        assertEquals("SUCCESS", responseObject.getStatus());
        assertEquals(data, responseObject.getData());
    }

    @Test
    void getDetailUser() {
        ResponseEntity<ResponseObject> responseEntity = candidateController.getDetailUser(mockRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseObject responseObject = responseEntity.getBody();
        Map<String, String> data = new LinkedHashMap<>();
        data.put("username", "khang");
        data.put("email", "khang123@gmail.com");
        data.put("phonenumber", "0999999999");
        assertEquals("SUCCESS", responseObject.getStatus());
        assertEquals("Loading data success!", responseObject.getMessage());
        assertEquals(data, responseObject.getData());
    }
}