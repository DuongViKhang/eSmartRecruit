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
import java.util.Arrays;
import java.util.Collections;
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
    void forgotPassword_SuccessfulPasswordChange() throws UserException {
        // Mock successful password change
        when(userService.updateUserpassword(anyString(), anyString())).thenReturn("Successfully saved");
//
        // Create a mock ChangePasswordRequest
        ChangePasswordRequest mockChangePasswordRequest = new ChangePasswordRequest();
        mockChangePasswordRequest.setUsername("admin");
        mockChangePasswordRequest.setNewPassword("$10$/nCR/hYK8RJFwvHCxCwBQOirAhm9jdcxaSSKCBFJCgCLimHFTWUuy");

        // Call the forgotPassword method
        ResponseEntity<ResponseObject> responseEntity = guestController.forgotPassword(mockChangePasswordRequest);

        // Assert the results
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Success", responseEntity.getBody().getStatus());
        assertEquals("Successfully saved", responseEntity.getBody().getMessage());
    }

    @Test
    void forgotPassword_UnsuccessfulPasswordChange() throws UserException {
        // Mock unsuccessful password change
        when(userService.updateUserpassword(anyString(), anyString())).thenReturn("Could not save");

        // Create a mock ChangePasswordRequest
        ChangePasswordRequest mockChangePasswordRequest = new ChangePasswordRequest();
        mockChangePasswordRequest.setUsername("admin");
        mockChangePasswordRequest.setNewPassword("$10$/nCR/hYK8RJFwvHCxCwBQOirAhm9jdcxaSSKCBFJCgCLimHFTWUuy");

        // Call the forgotPassword method
        ResponseEntity<ResponseObject> responseEntity = guestController.forgotPassword(mockChangePasswordRequest);

        // Assert the results
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Success", responseEntity.getBody().getStatus());
        assertEquals("Could not save", responseEntity.getBody().getMessage());
    }

    @Test
    void forgotPassword_UserException() throws UserException {
        // Mock throwing a UserException
        when(userService.updateUserpassword(anyString(), anyString())).thenThrow(UserException.class);

        // Create a mock ChangePasswordRequest
        ChangePasswordRequest mockChangePasswordRequest = new ChangePasswordRequest();
        mockChangePasswordRequest.setUsername("admin");
        mockChangePasswordRequest.setNewPassword("$10$/nCR/hYK8RJFwvHCxCwBQOirAhm9jdcxaSSKCBFJCgCLimHFTWUuy");

        // Call the forgotPassword method
        ResponseEntity<ResponseObject> responseEntity = guestController.forgotPassword(mockChangePasswordRequest);

        // Assert the results for the exception case
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
        assertEquals("Error", responseEntity.getBody().getStatus()); // Adjust this based on your actual implementation
        // Additional assertions based on how you handle exceptions in your application
    }

    @Test
    void searchJob() throws Exception {

        when(positionService.searchPositions(anyString())).thenReturn(Arrays.asList(new Position(), new Position()));
        ResponseEntity<ResponseObject> responseEntity = guestController.searchJob(anyString());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Success", responseEntity.getBody().getStatus());
        assertEquals("Search successfully!", responseEntity.getBody().getMessage());
         // Add more assertions based on your expected search result
        verify(positionService, times(1)).searchPositions(anyString());

    }
    @Test
    void searchJob_EmptyResult() throws Exception {
        // Arrange
        when(positionService.searchPositions(anyString())).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<ResponseObject> responseEntity = guestController.searchJob(anyString());

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Success", responseEntity.getBody().getStatus());
        assertEquals("Search successfully!", responseEntity.getBody().getMessage());
        verify(positionService, times(1)).searchPositions(anyString());
    }

    @Test
    void searchJob_Exception() throws Exception {
        // Arrange
//        String keyword = "FPT";
        when(positionService.searchPositions(anyString())).thenThrow(new Exception());

        ResponseEntity<ResponseObject> responseEntity = guestController.searchJob(anyString());

        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
        assertEquals("Error", responseEntity.getBody().getStatus());
        verify(positionService, times(1)).searchPositions(anyString());
    }

}