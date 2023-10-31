package com.example.eSmartRecruit.controllers.request_reponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseObject {
    private String status;
    private String message;
    private Object data;
}
