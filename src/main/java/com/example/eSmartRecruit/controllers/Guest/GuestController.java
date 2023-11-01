package com.example.eSmartRecruit.controllers.Guest;

import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.services.impl.PositionService;
import com.example.eSmartRecruit.services.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("eSmartRecruit/")
@AllArgsConstructor
public class GuestController {
    private UserService userService;
    private PositionService positionService;

    @PutMapping("/resetpassword")
    public ResponseEntity<ResponseObject> forgotPassword(//@RequestBody String username,@RequestBody String newpassword,
                                                         @RequestBody User user,
                                                         HttpServletRequest request, HttpServletResponse response) {
        try {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("Success").message(userService.updateUserpassword(user.getUsername(), user.getPassword())).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("Error").message(userService.updateUserpassword(user.getUsername(), user.getPassword())).build(), HttpStatus.NOT_IMPLEMENTED);
        }

    }


    @GetMapping("/search/{keyword}")
    public ResponseEntity<ResponseObject> searchJob(@PathVariable("keyword") String keyword, HttpServletRequest request)
    {
        try{
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("Success").message("Search succesfully!").data(positionService.searchPositions(keyword)).build(), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}