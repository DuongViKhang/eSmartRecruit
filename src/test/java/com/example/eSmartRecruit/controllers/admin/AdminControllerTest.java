package com.example.eSmartRecruit.controllers.admin;
import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.config.JwtService;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.exception.PositionException;
import com.example.eSmartRecruit.exception.UserException;
import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.models.enumModel.Role;
import com.example.eSmartRecruit.models.enumModel.UserStatus;
import com.example.eSmartRecruit.services.IStorageService;
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
import java.util.ArrayList;
import java.util.List;


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
    private PositionService positionService;
    @Test
    void PositionAdmin() throws  UserException{
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
    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

}



