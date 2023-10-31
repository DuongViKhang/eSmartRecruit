package com.example.eSmartRecruit.controllers.candidate;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CandidateResponse {
    private String message;
    private String status;
}
