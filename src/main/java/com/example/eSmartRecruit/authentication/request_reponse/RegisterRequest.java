package com.example.eSmartRecruit.authentication.request_reponse;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Username must be filled")
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String username;

    @NotBlank(message = "This must be filled!")
    @Pattern(regexp = "^(.+)@(.+)$")
    private String email;

    @NotBlank(message = "Must be filled")
    @Pattern(regexp = "[a-z0-9_-]{6,12}$")
    private String password;

    @NotBlank(message = "This must be filled!")
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b")
    private String phoneNumber;

    @NotBlank(message = "Role must be declared!")
    private String roleName;

}
