package com.example.eSmartRecruit.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.eSmartRecruit.exception.ApplicationException;
import com.example.eSmartRecruit.models.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.eSmartRecruit.repositories.ApplicationRepos;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;
import java.sql.Date;
import java.time.LocalDate;
class ApplicationServiceTest {
    private ApplicationService applicationService;
    private ApplicationRepos applicationRepository;
    @BeforeEach
    void setUp() {
        applicationRepository = mock(ApplicationRepos.class);
        applicationService = new ApplicationService(applicationRepository);
    }
    @Test
    void testApply_SuccessfulApplication() {
        // Arrange
        Application application = new Application();
        application.setCandidateID(1);
        application.setPositionID(1);

        // Mocking behavior to simulate a successful application
        when(applicationRepository.findByCandidateIDAndPositionID(1, 1)).thenReturn(java.util.Optional.empty());
        when(applicationRepository.save(application)).thenReturn(application);

        // Act
        String result = applicationService.apply(application);

        // Assert
        assertEquals("Successfully applied", result);
        verify(applicationRepository, times(1)).findByCandidateIDAndPositionID(1, 1);
        verify(applicationRepository, times(1)).save(application);
    }
    @Test
    void testApply_AlreadyApplied() {
        // Arrange
        Application application = new Application();
        application.setCandidateID(1);
        application.setPositionID(1);

        // Mocking behavior to simulate an already applied situation
        when(applicationRepository.findByCandidateIDAndPositionID(1, 1)).thenReturn(java.util.Optional.of(application));

        // Act
        String result = applicationService.apply(application);

        // Assert
        assertEquals("You have already applied to this position!", result);
        verify(applicationRepository, times(1)).findByCandidateIDAndPositionID(1, 1);
        verify(applicationRepository, never()).save(any());
    }
    @Test
    void testIsApplied_ApplicationExists() {
        // Arrange
        Integer candidateID = 1;
        Integer positionID = 1;
        when(applicationRepository.findByCandidateIDAndPositionID(candidateID, positionID))
                .thenReturn(Optional.of(new Application()));

        ApplicationService applicationService = new ApplicationService(applicationRepository);

        // Act
        boolean result = applicationService.isApplied(candidateID, positionID);

        // Assert
        assertTrue(result);
    }
    @Test
    void testIsApplied_ApplicationDoesNotExist() {
        // Arrange
        Integer candidateID = 1;
        Integer positionID = 1;
        when(applicationRepository.findByCandidateIDAndPositionID(candidateID, positionID)).thenReturn(Optional.empty());

        ApplicationService applicationService = new ApplicationService(applicationRepository);

        // Act
        boolean result = applicationService.isApplied(candidateID, positionID);

        // Assert
        assertFalse(result);
    }
    @Test
    void testGetApplicationsByCandidateId() {
        // Arrange
        Integer candidateID = 1;
        List<Application> expectedApplications = Arrays.asList(new Application(), new Application());
        when(applicationRepository.findByCandidateID(candidateID)).thenReturn(expectedApplications);

        ApplicationService applicationService = new ApplicationService(applicationRepository);

        // Act
        List<Application> result = applicationService.getApplicationsByCandidateId(candidateID);

        // Assert
        assertEquals(expectedApplications, result);
    }
    @Test
    void testGetApplicationById_ApplicationExists() throws ApplicationException, ApplicationException {
        // Arrange
        Integer applicationID = 1;
        Integer candidateID = 1;
        Application expectedApplication = new Application();
        when(applicationRepository.findById(applicationID)).thenReturn(Optional.of(expectedApplication));

        ApplicationService applicationService = new ApplicationService(applicationRepository);

        // Act
        Application result = applicationService.getApplicationById(applicationID);

        // Assert
        assertEquals(expectedApplication, result);
    }
    @Test
    void testGetApplicationById_ApplicationDoesNotExist() {
        // Arrange
        Integer applicationID = 1;
        Integer candidateID = 1;
        when(applicationRepository.findById(applicationID)).thenReturn(Optional.empty());

        ApplicationService applicationService = new ApplicationService(applicationRepository);

        // Act and Assert
        assertThrows(ApplicationException.class, () -> applicationService.getApplicationById(applicationID));
    }
    @Test
    void testUpdate_Success() {
        // Arrange
        Integer candidateId = 1;
        Integer applicationId = 1;
        Application newApplication = new Application();
        newApplication.setCv("Updated CV");

        Application existingApplication = new Application();
        existingApplication.setCandidateID(candidateId);

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(existingApplication));

        ApplicationService applicationService = new ApplicationService(applicationRepository);

        // Act
        String result = applicationService.update(candidateId, newApplication, applicationId);

        // Assert
        assertEquals("update Success", result);
        assertEquals("Updated CV", existingApplication.getCv());
    }
    @Test
    void testUpdate_ApplicationNotFound() {
        // Arrange
        Integer candidateId = 1;
        Integer applicationId = 1;
        Application newApplication = new Application();

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.empty());

        ApplicationService applicationService = new ApplicationService(applicationRepository);

        // Act
        String result = applicationService.update(candidateId, newApplication, applicationId);

        // Assert
        assertTrue(result.contains("Application not found!"));
    }
    @Test
    void testUpdate_NotYourApplication() {
        // Arrange
        Integer candidateId = 1;
        Integer applicationId = 1;
        Application newApplication = new Application();
        newApplication.setCv("Updated CV");

        Application existingApplication = new Application();
        existingApplication.setCandidateID(2);

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(existingApplication));

        ApplicationService applicationService = new ApplicationService(applicationRepository);

        // Act
        String result = applicationService.update(candidateId, newApplication, applicationId);

        // Assert
        assertTrue(result.contains("This is not your application!"));
    }

    @Test
    void testIsPresent_ApplicationFound() {
        // Arrange
        Integer applicationId = 1;

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(new Application()));

        ApplicationService applicationService = new ApplicationService(applicationRepository);

        // Act
        boolean result = applicationService.isPresent(applicationId);

        // Assert
        assertTrue(result);
    }
    @Test
    void testIsPresent_ApplicationNotFound() {
        // Arrange
        Integer applicationId = 1;

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.empty());

        ApplicationService applicationService = new ApplicationService(applicationRepository);

        // Act
        boolean result = applicationService.isPresent(applicationId);

        // Assert
        assertFalse(result);
    }
    @Test
    void testIsPresent_ApplicationException() {
        // Arrange
        Integer applicationId = 1;

        when(applicationRepository.findById(applicationId)).thenThrow(new ApplicationException("Test exception"));

        ApplicationService applicationService = new ApplicationService(applicationRepository);

        // Act
        boolean result = applicationService.isPresent(applicationId);

        // Assert
        assertFalse(result);
    }

    @Test
    void deletejob() {
    }
}