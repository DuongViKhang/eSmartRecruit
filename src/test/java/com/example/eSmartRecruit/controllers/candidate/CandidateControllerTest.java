package com.example.eSmartRecruit.controllers.candidate;

import com.example.eSmartRecruit.controllers.request_reponse.OnePositionResponse;
import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.services.impl.PositionService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandidateControllerTest {
    @InjectMocks
    private CandidateController candidateController;

    @Mock
    private PositionService positionService;

    @Test
    void getDetailPosition() {
        Position mockPosition = new Position();
        mockPosition.setId(1);
        mockPosition.setTitle("Software Engineer");
        mockPosition.setJobDescription("Build web applications");
        mockPosition.setJobRequirements("3 years of experience");
        mockPosition.setSalary(BigDecimal.valueOf(5000));
        mockPosition.setPostDate(Date.valueOf("2023-10-10"));
        mockPosition.setExpireDate(Date.valueOf("2023-10-30"));
        mockPosition.setUpdateDate(Date.valueOf("2023-10-10"));
        mockPosition.setLocation("FPT, Thu Duc City");

        when(positionService.getSelectedPosition(1)).thenReturn(mockPosition);

        ResponseEntity<OnePositionResponse> responseEntity = candidateController.getDetailPosition(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        OnePositionResponse onePositionResponse = responseEntity.getBody();
        assertNotNull(onePositionResponse);
        assertEquals("SUCCESS", onePositionResponse.getStatus());

        Position returnedPosition = onePositionResponse.getPosition();
        assertNotNull(returnedPosition);
        assertEquals(1, returnedPosition.getId());
        assertEquals("Software Engineer", returnedPosition.getTitle());

        verify(positionService, times(1)).getSelectedPosition(1);
    }
}