package com.example.eSmartRecruit.services.impl;

import com.example.eSmartRecruit.controllers.request_reponse.request.InterviewSessionRequest;
import com.example.eSmartRecruit.exception.InterviewSessionException;
import com.example.eSmartRecruit.models.InterviewSession;
import com.example.eSmartRecruit.models.enumModel.SessionStatus;
import com.example.eSmartRecruit.repositories.InterviewSessionRepos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    void testScheduleInterview_Success() throws InterviewSessionException {
        // Arrange
        int id = 1;
        InterviewSessionRequest interviewSessionRequest = new InterviewSessionRequest();
        interviewSessionRequest.setInterviewerId(123);
        interviewSessionRequest.setDate(Date.valueOf("2022-12-12"));
        interviewSessionRequest.setLocation("Test Location");
        interviewSessionRequest.setNotes("Test Notes");

        InterviewSession interviewSession = new InterviewSession();
        interviewSession.setId(id);

        // Mock the repository method calls
        when(interviewSessionRepos.findById(anyInt())).thenReturn(Optional.of(interviewSession));
        when(interviewSessionRepos.save(any())).thenReturn(interviewSession);

        // Act
        InterviewSession result = interviewSessionService.scheduleInterview(id, interviewSessionRequest);

        // Assert
        verify(interviewSessionRepos, times(1)).findById(anyInt());
        verify(interviewSessionRepos, times(1)).save(any());

        // Assert the updated interviewSession object
        assertEquals(interviewSessionRequest.getInterviewerId(), result.getInterviewerID());
        assertEquals(interviewSessionRequest.getDate(), result.getDate());
        assertEquals(interviewSessionRequest.getLocation(), result.getLocation());
        assertEquals(SessionStatus.Yet, result.getStatus());
        assertEquals(interviewSessionRequest.getNotes(), result.getNotes());
    }
}




