package com.example.eSmartRecruit.dto;

import com.example.eSmartRecruit.models.User;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.sql.Date;
import java.util.Optional;

@Data
public class EditUserDto {
    private Optional<Integer> id;
    private String username;
    private String email;
    private String phoneNumber;
    private Date updateDate;

    public EditUserDto() {
        // Default constructor
    }

    public EditUserDto(User user) {
        this.id = Optional.of(user.getId());
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.updateDate = user.getUpdateDate();
    }
}
