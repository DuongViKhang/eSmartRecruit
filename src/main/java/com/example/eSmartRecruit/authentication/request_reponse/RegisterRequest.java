package com.example.eSmartRecruit.authentication.request_reponse;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotNull(message = "Username must be filled")
    private String username;
    @NotNull(message = "email must be filled")
    private String email;
    @NotNull(message = "Must be filled")
    private String password;
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b")
    private String phoneNumber;

    @NotNull(message = "Role must be declared!")
    private String roleName;

}
