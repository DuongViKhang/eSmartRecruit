package com.example.eSmartRecruit.controllers.user;

import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.dto.EditUserDto;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.services.impl.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("eSmartRecruit/admin")
@AllArgsConstructor
public class UserController {
    private  final UserService userService;

    @GetMapping("/user/{userId}")
    public @ResponseBody ResponseEntity<ResponseObject> getUser(@PathVariable Integer userId) {
       try {
           return new ResponseEntity<>(ResponseObject.builder().status("Success").data(userService.getUserById(userId)).build(), HttpStatus.OK);
       }catch (Exception e){
           return new ResponseEntity<>(ResponseObject.builder().status("Error").message(e.getMessage()).build(), HttpStatus.NOT_IMPLEMENTED);
       }
    }

    @PutMapping(value = "/esmartrecruit/admin/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<ResponseObject>  updateFlight(
            @RequestBody EditUserDto editUserDto){
        try {
            return new ResponseEntity<>(ResponseObject.builder().status("Success").data(userService.editUserInformation(editUserDto)).build(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(ResponseObject.builder().status("Error").message(e.getMessage()).build(), HttpStatus.NOT_IMPLEMENTED);
        }
    }
}
