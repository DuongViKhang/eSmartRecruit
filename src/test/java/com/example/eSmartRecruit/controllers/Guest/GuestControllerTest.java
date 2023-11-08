package com.example.eSmartRecruit.controllers.Guest;

import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.controllers.candidate.CandidateController;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.controllers.request_reponse.request.ChangePasswordRequest;
import com.example.eSmartRecruit.exception.UserException;
import com.example.eSmartRecruit.models.Application;
import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.models.enumModel.Role;
import com.example.eSmartRecruit.models.enumModel.UserStatus;
import com.example.eSmartRecruit.repositories.UserRepos;
import com.example.eSmartRecruit.services.impl.PositionService;
import com.example.eSmartRecruit.services.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@PrepareForTest({ExtractUser.class})
class GuestControllerTest {
    @InjectMocks
    private GuestController guestController;

    @Mock
    private UserService userService;
    @Mock
    private PositionService positionService;
    @Mock
    private UserRepos userRepos;
    @Test
    void forgotPassword() throws UserException {

        ChangePasswordRequest mockChangePasswordRequest = new ChangePasswordRequest();
        mockChangePasswordRequest.setUsername("khang");
        mockChangePasswordRequest.setNewPassword("$2a$10$S5x1eUGgsbXA4RJfrnc07ueCheYAVNMXsqw23/HfivFQJsaowrTXW");


        List<String> stringReturn = Arrays.asList("Successfully saved", "Could not save");
        for (String str:stringReturn) {
            when(userService.updateUserpassword(anyString(), anyString())).thenReturn(str);
            ResponseEntity<ResponseObject> responseEntity = guestController.forgotPassword(mockChangePasswordRequest);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals("Success", responseEntity.getBody().getStatus());
            assertEquals(str, responseEntity.getBody().getMessage());
        }

        // check respond error
//        when(userRepos.findByUsername(any(String.class))).thenThrow(any(UserException.class));
//        when(userService.updateUserpassword(anyString(), anyString())).thenThrow(UserException.class);
//        when(guestController.forgotPassword(mockChangePasswordRequest)).thenThrow(new UserException("User not found!"));
        ResponseEntity<ResponseObject> responseEntity = guestController.forgotPassword(mockChangePasswordRequest);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
        assertEquals("Error", responseEntity.getBody().getStatus());

    }

    @Test
    void searchJob() throws Exception {
        when(positionService.searchPositions(anyString())).thenReturn(Arrays.asList(new Position(), new Position()));
        ResponseEntity<ResponseObject> responseEntity = guestController.searchJob(anyString());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Success", responseEntity.getBody().getStatus());

        when(positionService.searchPositions(anyString())).thenThrow(Exception.class);
        responseEntity = guestController.searchJob(anyString());
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
        assertEquals("Error", responseEntity.getBody().getStatus());

    }
}