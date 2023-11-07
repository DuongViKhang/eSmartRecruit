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
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//

import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;

import com.example.eSmartRecruit.controllers.request_reponse.request.ReportRequest;
import com.example.eSmartRecruit.controllers.request_reponse.request.UserRequest;
import com.example.eSmartRecruit.exception.UserException;
import com.example.eSmartRecruit.models.InterviewSession;
import com.example.eSmartRecruit.models.Report;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.services.impl.InterviewSessionService;
import com.example.eSmartRecruit.services.impl.ReportService;
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
    private InterviewSessionService interviewSessionService;
    private ReportService reportService;

    @GetMapping("/home")
    ResponseEntity<ResponseObject> getInterviewerSession(HttpServletRequest request){
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            if(!userInfo.isEnabled()){
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message("Account not active!").status("ERROR").build(),HttpStatus.BAD_REQUEST);
            }
            Integer interviewerId = userInfo.getUserId();
            List<InterviewSession> interviewSessionList = interviewSessionService.findByInterviewerID(interviewerId);
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().message("").status("SUCCESS").data(interviewSessionList).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().message(e.getMessage()).status("ERROR").build(),HttpStatus.NOT_IMPLEMENTED);
        }

    }
    @GetMapping("/{interviewersessionID}")
    ResponseEntity<ResponseObject> findByInterviewSessionID(@PathVariable("interviewersessionID")Integer interviewersessionID, HttpServletRequest request){
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            if(!userInfo.isEnabled()){
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message("Account not active!").status("ERROR").build(),HttpStatus.BAD_REQUEST);
            }
            InterviewSession interviewSession = interviewSessionService.findByID(interviewersessionID);
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().message("").status("SUCCESS").data(interviewSession).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().message(e.getMessage()).status("ERROR").build(),HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @PostMapping("/report/create/{interviewersessionID}")
    ResponseEntity<ResponseObject> reportInterviewSession(@PathVariable("interviewersessionID")Integer interviewersessionID, HttpServletRequest request, @RequestBody ReportRequest reportRequest){
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            if(!userInfo.isEnabled()){
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message("Account not active!").status("ERROR").build(),HttpStatus.BAD_REQUEST);
            }

            if(!interviewSessionService.isAlready(interviewersessionID)){
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message("Interview Session not already done!").status("ERROR").build(),HttpStatus.BAD_REQUEST);
            }

            Report report = Report.builder().reportName(reportRequest.getReportName()).reportData(reportRequest.getReportData()).sessionID(interviewersessionID).createDate(Date.valueOf(LocalDate.now())).updateDate(Date.valueOf(LocalDate.now())).build();
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("SUCCESS").message(reportService.reportInterviewSession(report)).build(), HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().message(e.getMessage()).status("ERROR").build(),HttpStatus.NOT_IMPLEMENTED);
        }

    }

//get userInterviewer info
    @GetMapping("/profile")
    ResponseEntity<ResponseObject> getDetailUserInterviewer(HttpServletRequest request) throws JSONException, UserException {
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
                                              @RequestBody UserRequest user0
    ) throws JSONException, UserException {
        String authHeader = request.getHeader("Authorization");
        //return new ResponseEntity<String>("hello",HttpStatus.OK);
        ExtractUser userInfo = new ExtractUser(authHeader, userService);
        if(!userInfo.isEnabled()){
            return null;
        }
        Integer userId = userInfo.getUserId();
        User user = userService.updateUser(user0,userId);
        return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("Success").data(user).build(),HttpStatus.OK);

    }
}
