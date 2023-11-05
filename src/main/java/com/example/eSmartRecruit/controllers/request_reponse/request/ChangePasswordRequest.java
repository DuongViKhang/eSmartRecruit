package com.example.eSmartRecruit.controllers.request_reponse.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "This must be filled!")
    @Pattern(message = "Please filled right format!",regexp = "^[a-zA-Z0-9]+$")
    private String username; //email có dạng text@text
    @NotBlank(message = "This must be filled!")
    @Pattern(message = "Please filled right format!",regexp = "((?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!]).{6,20})")
    private String newPassword; // Số đt có dạng mở đầu bằng 84 hoặc 0, số tiếp theo là 3/5/7/8/9 và sau đó là 8 số bất kì
}
