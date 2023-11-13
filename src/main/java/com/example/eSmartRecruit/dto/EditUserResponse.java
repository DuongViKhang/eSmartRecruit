package com.example.eSmartRecruit.dto;

import com.example.eSmartRecruit.models.User;
import lombok.Data;

@Data
public class EditUserResponse {
    private String username;
    private String email;
    private String phoneNumber;

    public EditUserResponse() {
        // Default constructor
    }

    public EditUserResponse(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
    }
}
