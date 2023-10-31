package com.example.eSmartRecruit.controllers.request_reponse;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CandidateApplyResponse {
    private String message;
    private String status;
}
