package com.example.eSmartRecruit.controllers.user;

import com.example.eSmartRecruit.dto.EditUserDto;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.services.impl.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("eSmartRecruit/user")

public class UserController {
    private  final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/id={id}")
    public @ResponseBody User getUser(@PathVariable Long id) {
        return userService.getUserDetail(id);
    }

    @PatchMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Boolean updateFlight(
            @PathVariable("id") Integer id,
            @RequestBody EditUserDto editUserDto){
        return userService.editUserInformation(id,editUserDto);
    }
}



