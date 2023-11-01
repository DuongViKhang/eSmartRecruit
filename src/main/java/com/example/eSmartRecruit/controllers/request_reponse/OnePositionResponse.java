package com.example.eSmartRecruit.controllers.request_reponse;

import com.example.eSmartRecruit.models.Position;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OnePositionResponse {
    private String status;
    private Position position;
}
