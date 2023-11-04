package com.example.eSmartRecruit.controllers.request_reponse.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotNull(message = "This must be filled!")
    @Pattern(regexp = "^(.+)@(.+)$")
    private String email;
    @NotNull(message = "This must be filled!")
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b")
    private String phoneNumber;
}
