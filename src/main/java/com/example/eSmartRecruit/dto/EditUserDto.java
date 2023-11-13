package com.example.eSmartRecruit.dto;


import com.example.eSmartRecruit.models.User;
import lombok.Data;


@Data
public class EditUserDto {
    private String username;
    private String email;
    private String phoneNumber;

    public EditUserDto() {
        // Default constructor
    }

    public EditUserDto(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
    }
}