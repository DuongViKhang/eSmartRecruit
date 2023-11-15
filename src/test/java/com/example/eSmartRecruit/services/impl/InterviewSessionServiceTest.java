package com.example.eSmartRecruit.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.eSmartRecruit.controllers.request_reponse.request.InterviewSessionRequest;
import com.example.eSmartRecruit.exception.InterviewSessionException;
import com.example.eSmartRecruit.models.InterviewSession;
import com.example.eSmartRecruit.models.enumModel.Role;
import com.example.eSmartRecruit.models.enumModel.SessionResult;
import com.example.eSmartRecruit.models.enumModel.UserStatus;
import com.example.eSmartRecruit.repositories.InterviewSessionRepos;
import com.example.eSmartRecruit.models.enumModel.SessionStatus;
import com.example.eSmartRecruit.repositories.UserRepos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InterviewSessionServiceTest {

    private InterviewSessionService interviewSessionService;
    private InterviewSessionRepos interviewSessionRepos;

    @BeforeEach
    public void setup() {
        interviewSessionRepos = mock(InterviewSessionRepos.class);
        interviewSessionService = new InterviewSessionService(interviewSessionRepos);
    }

    @Test
    void testFindByInterviewerID() throws InterviewSessionException {
        // Arrange
        int userId = 1;
        List<InterviewSession> expectedSessions = new ArrayList<>();
        when(interviewSessionRepos.findByInterviewerID(userId)).thenReturn(expectedSessions);

        // Act
        List<InterviewSession> result = interviewSessionService.findByInterviewerID(userId);

        // Assert
        assertEquals(expectedSessions, result);
    }

    @Test
    void testFindByID_Successful() throws InterviewSessionException {
        // Arrange
        int sessionID = 1;
        InterviewSession expectedSession = new InterviewSession();
        expectedSession.setId(sessionID);
        when(interviewSessionRepos.findById(sessionID)).thenReturn(Optional.of(expectedSession));

        // Act
        InterviewSession result = interviewSessionService.findByID(sessionID);

        // Assert
        assertEquals(expectedSession, result);
    }

    @Test
    void testFindByID_SessionNotFound() {
        // Arrange
        int sessionID = 1;
        when(interviewSessionRepos.findById(sessionID)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(InterviewSessionException.class, () -> interviewSessionService.findByID(sessionID));
    }

    @Test
    void testIsAlready_SessionNotAlready() throws InterviewSessionException {
        // Arrange
        int sessionID = 1;
        InterviewSession interviewSession = new InterviewSession();
        interviewSession.setId(sessionID);
        interviewSession.setStatus(SessionStatus.Yet);
        when(interviewSessionRepos.findById(sessionID)).thenReturn(Optional.of(interviewSession));

        // Act
        boolean result = interviewSessionService.isAlready(sessionID);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsAlready_SessionAlready() throws InterviewSessionException {
        // Arrange
        int sessionID = 1;
        InterviewSession interviewSession = new InterviewSession();
        interviewSession.setId(sessionID);
        interviewSession.setStatus(SessionStatus.Already);
        when(interviewSessionRepos.findById(sessionID)).thenReturn(Optional.of(interviewSession));

        // Act
        boolean result = interviewSessionService.isAlready(sessionID);

        // Assert
        assertTrue(result);
    }
    @Test
    void testGetCountInterview() {
        // Arrange
        long expectedCount = 5;  // Đặt giá trị mong đợi

        // Mock behavior of interviewSessionRepos.count()
        when(interviewSessionRepos.count()).thenReturn(expectedCount);

        // Act
        long actualCount = interviewSessionService.getCountInterview();

        // Assert
        assertEquals(expectedCount, actualCount);

        // Verify that interviewSessionRepos.count() was called exactly once
        verify(interviewSessionRepos, times(1)).count();
    }
    @Test
    void testSaveInterviewSession() {
        // Arrange
        InterviewSession interviewSession = new InterviewSession(/* set your interview session properties here */);

        // Act
        interviewSessionService.save(interviewSession);

        // Assert

        // Verify that interviewSessionRepos.save() was called exactly once with the correct interviewSession
        verify(interviewSessionRepos, times(1)).save(interviewSession);
    }
//    @Test
//    void testScheduleInterview_Success() throws InterviewSessionException {
//        // Arrange
//        int sessionId = 1;
//        InterviewSessionRequest interviewSessionRequest = new InterviewSessionRequest(3,Date.valueOf("2023-11-02"),"FPT - Thu Duc City","Good");
//        InterviewSession existingInterviewSession = new InterviewSession(sessionId,1,2,Date.valueOf("2023-11-12"),"Vo Van Ngan",SessionStatus.Already, SessionResult.Good,"Tốt");
//        // Mocking the behavior of findByID
//
////        InterviewSessionService tempService = new InterviewSessionService();
////        InterviewSessionService spyTemp = Mockito.spy(tempService);
////
////        Mockito.doReturn(existingInterviewSession).when(spyTemp).findByID(1);
//        when(interviewSessionRepos.findById(1)).thenReturn(Optional.of(existingInterviewSession));
//        when(interviewSessionService.findByID(sessionId)).thenReturn(Optional.of(existingInterviewSession));
//
//        // Mocking the behavior of save
//        when(interviewSessionRepos.save(any())).thenReturn(existingInterviewSession);
//
//        // Act
//        InterviewSession scheduledInterviewSession = interviewSessionService.scheduleInterview(sessionId, interviewSessionRequest);
//
//        // Assert
//        assertEquals(interviewSessionRequest.getInterviewerId(), scheduledInterviewSession.getInterviewerID());
//        assertEquals(interviewSessionRequest.getDate(), scheduledInterviewSession.getDate());
//        assertEquals(interviewSessionRequest.getLocation(), scheduledInterviewSession.getLocation());
//        assertEquals(SessionStatus.Yet, scheduledInterviewSession.getStatus());
//        assertEquals(interviewSessionRequest.getNotes(), scheduledInterviewSession.getNotes());
//
//        // Verify that findByID was called exactly once with the correct sessionId
//        verify(interviewSessionService, times(1)).findByID(sessionId);
//
//        // Verify that save was called exactly once with the correct interview session
//        verify(interviewSessionRepos, times(1)).save(existingInterviewSession);
//    }
}
