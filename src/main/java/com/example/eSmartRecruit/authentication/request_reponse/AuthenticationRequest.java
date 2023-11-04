package com.example.eSmartRecruit.authentication.request_reponse;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NonNull
    @NotBlank(message = "Must be filled")
    private String username;
    private String password;
}
