package com.example.eSmartRecruit.controllers.candidate;
import java.util.HashMap;
import java.util.Map;

import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.controllers.request_reponse.CandidateApplyResponse;
import com.example.eSmartRecruit.controllers.request_reponse.OnePositionResponse;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.models.Application;

import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.services.impl.ApplicationService;
import com.example.eSmartRecruit.services.IStorageService;
import com.example.eSmartRecruit.services.impl.PositionService;
import com.example.eSmartRecruit.services.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;

import lombok.AllArgsConstructor;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.repositories.PositionRepos;
import com.example.eSmartRecruit.services.impl.ApplicationService;
import com.example.eSmartRecruit.services.IStorageService;
import com.example.eSmartRecruit.services.impl.PositionService;
import jakarta.servlet.http.HttpServletRequest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("eSmartRecruit/")
@AllArgsConstructor
public class CandidateController {
    private PositionService positionService;
    private ApplicationService applicationService;

    private UserService userService;
    private IStorageService storageService;
    @GetMapping("/home")
    List<String> getAllCandidate(){
        return List.of("Hello candidate","");
    }

    @GetMapping("/position/{positionID}")
    ResponseEntity<OnePositionResponse> getDetailPosition(@PathVariable("positionID")Integer id){
        Position pos = positionService.getSelectedPosition(id);
        //return new ResponseEntity<String>("hello",HttpStatus.OK);
        return new ResponseEntity<OnePositionResponse>(OnePositionResponse.builder().status("SUCCESS").position(pos).build(),HttpStatus.OK);


        //return new ResponseEntity<Positions>(positionService.getSelectedPosition(id),HttpStatus.OK);
    }

    @PostMapping("/application/create/{positionID}")
    ResponseEntity<CandidateApplyResponse> applyForPosition(@PathVariable("positionID")Integer id, HttpServletRequest request, @RequestParam("cv")MultipartFile cv){
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            if(!userInfo.isEnabled()){
                return new ResponseEntity<CandidateApplyResponse>(CandidateApplyResponse.builder()
                        .message("Account not active!").status("ERROR").build(),HttpStatus.BAD_REQUEST);
            }
            String generatedFileName = storageService.storeFile(cv);
            int candidateId = userInfo.getUserId();

            Application application = new Application(candidateId, id, generatedFileName);

            return new ResponseEntity<CandidateApplyResponse>(CandidateApplyResponse.builder()
                    .message(applicationService.apply(application)).status("SUCCESS").build(),HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<CandidateApplyResponse>(CandidateApplyResponse.builder().message(e.getMessage()).status("ERROR").build(),HttpStatus.NOT_IMPLEMENTED);

        }

    }
    //get user info
    @GetMapping("/profile")
    ResponseEntity<ResponseObject> getDetailUser(HttpServletRequest request) throws JSONException {
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
        data.put("phonenumber", user.getPhoneNumber());

        return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("Success").message("Loading data success!").data(data).build(),HttpStatus.OK);

    }
    //update user
    @PutMapping("/profile")
    ResponseEntity<ResponseObject> updateUser(HttpServletRequest request,
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

        Map<String, Object> data = new HashMap<>();
        data.put("email", user.getEmail());
        data.put("phoneNumber",user.getPhoneNumber());

        return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("Success").message("Update infomation succesfully!").data(data).build(),HttpStatus.OK);

    }
}
