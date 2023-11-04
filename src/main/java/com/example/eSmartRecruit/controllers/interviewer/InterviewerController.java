package com.example.eSmartRecruit.controllers.interviewer;

//import com.example.eSmartRecruit.config.ExtractUser;
//import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
//import com.example.eSmartRecruit.models.User;
//import com.example.eSmartRecruit.services.impl.UserService;
//import jakarta.servlet.http.HttpServletRequest;
//import org.json.JSONException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import com.example.eSmartRecruit.models.User;
//import com.example.eSmartRecruit.services.impl.ApplicationService;
//import com.example.eSmartRecruit.services.IStorageService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//

import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;

import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.services.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.*;

@AllArgsConstructor
@RestController
@RequestMapping("eSmartRecruit/interviewer")
public class InterviewerController {

    private UserService userService;


    @GetMapping("/home")
    List<String> getAllInterviewer(){
        return List.of("Hello interviewer");
    }

//get userInterviewer info
    @GetMapping("/profile")
    ResponseEntity<ResponseObject> getDetailUserInterviewer(HttpServletRequest request) throws JSONException {
        String authHeader = request.getHeader("Authorization");
        //return new ResponseEntity<String>("hello",HttpStatus.OK);
        ExtractUser userInfo = new ExtractUser(authHeader, userService);
        if(!userInfo.isEnabled()){
            return null;
        }
        Integer userId = userInfo.getUserId();
        User user = userService.getUserById(userId);

        Map<String, String> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        data.put("phonename", user.getPhoneNumber());

        return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("Success").data(data).build(), HttpStatus.OK);

    }
    //update userInterviewer
    @PutMapping("/profile")
    ResponseEntity<ResponseObject> updateUserInterviewer(HttpServletRequest request,
    //                                              @RequestParam("email")String email,
    //                                              @RequestParam("phoneNumber")String phoneNumber
                                              @RequestBody User user0
    ) throws JSONException {
        String authHeader = request.getHeader("Authorization");
        //return new ResponseEntity<String>("hello",HttpStatus.OK);
        ExtractUser userInfo = new ExtractUser(authHeader, userService);
        if(!userInfo.isEnabled()){
            return null;
        }
        Integer userId = userInfo.getUserId();
        User user = userService.updateUser(User.builder()
                .email(user0.getEmail())
                .phoneNumber(user0.getPhoneNumber()).build(),userId);
        return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("Success").data(user).build(),HttpStatus.OK);

    }
}
