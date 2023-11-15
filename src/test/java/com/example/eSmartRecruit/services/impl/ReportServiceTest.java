package com.example.eSmartRecruit.services.impl;

import com.example.eSmartRecruit.exception.UserException;
import com.example.eSmartRecruit.models.Report;
import com.example.eSmartRecruit.repositories.ReportRepos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReportServiceTest {


    private ReportService reportService;
    private ReportRepos reportRepos;

    @Test
    void getReportBySessionId() {
    }

    @BeforeEach
    public void setup() {
        reportRepos = mock(ReportRepos.class);
        reportService = new ReportService(reportRepos);
    }

    @Test
    void testReportInterviewSession_Successful() {
        // Arrange
        Report report = new Report();

        // Act
        String result = reportService.reportInterviewSession(report);

        // Assert
        verify(reportRepos, times(1)).save(report);
        assertEquals("Report Successfully!", result);
    }

    @Test
    void testReportInterviewSession_Exception() {
        // Arrange
        Report report = new Report();
        String errorMessage = "Error occurred";

        // Simulate an exception when saving the report
        doThrow(new RuntimeException(errorMessage)).when(reportRepos).save(report);

        // Act
        String result = reportService.reportInterviewSession(report);

        // Assert
        verify(reportRepos, times(1)).save(report);
        assertEquals(errorMessage, result);
    }

    @Test
    void testgetReportBySessionId_Report() throws UserException {
        Report report = new Report();
        when(reportRepos.findBySessionID(1)).thenReturn(Optional.of(report));
        Report result = reportService.getReportBySessionId(1);

        verify(reportRepos, times(1)).findBySessionID(1);
        assertEquals(report, result);
    }

    @Test
    void testgetReportBySessionId_Exception() throws UserException {
//        when(reportRepos.findBySessionID(1)).thenReturn(null);
        assertThrows(UserException.class, () -> reportService.getReportBySessionId(1));
    }
}