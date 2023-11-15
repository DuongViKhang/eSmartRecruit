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
import com.example.eSmartRecruit.models.enumModel.Role;
import com.example.eSmartRecruit.services.impl.InterviewSessionService;
import com.example.eSmartRecruit.services.impl.ReportService;
import com.example.eSmartRecruit.services.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.validation.Valid;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.*;

import static com.example.eSmartRecruit.controllers.request_reponse.ResponseObject.*;

@AllArgsConstructor
@RestController
@RequestMapping("eSmartRecruit/interviewer")
public class InterviewerController {

    private UserService userService;
    private InterviewSessionService interviewSessionService;
    private ReportService reportService;

    @GetMapping("/home")
    public ResponseEntity<ResponseObject> getInterviewerSession(HttpServletRequest request) {
        try {

            //TODO
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);

            if (!userInfo.isEnabled()) {
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message(NOT_ACTIVE)
                        .status(ResponseObject.ERROR_STATUS).build(), HttpStatus.BAD_REQUEST);
            }

            Integer interviewerId = userInfo.getUserId();
            List<InterviewSession> interviewSessionList = interviewSessionService.findByInterviewerID(interviewerId);

            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .message("")
                    .status(ResponseObject.SUCCESS_STATUS)
                    .data(interviewSessionList)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .message(e.getMessage())
                    .status(ResponseObject.ERROR_STATUS).build(), HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @GetMapping("/{interviewersessionID}")
    public ResponseEntity<ResponseObject> findByInterviewSessionID(@PathVariable("interviewersessionID") Integer interviewersessionID, HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);

            if (!userInfo.isEnabled()) {
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message(NOT_ACTIVE)
                        .status(ResponseObject.ERROR_STATUS).build(), HttpStatus.BAD_REQUEST);
            }

            InterviewSession interviewSession = interviewSessionService.findByID(interviewersessionID);

            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .message("")
                    .status(ResponseObject.SUCCESS_STATUS)
                    .data(interviewSession).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .message(e.getMessage())
                    .status(ResponseObject.ERROR_STATUS).build(), HttpStatus.NOT_IMPLEMENTED);
        }
    }


    @PostMapping("/report/create/{interviewersessionID}")
    public ResponseEntity<ResponseObject> reportInterviewSession(@PathVariable("interviewersessionID") Integer interviewersessionID, HttpServletRequest request, @RequestBody @Valid ReportRequest reportRequest) {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);

            if (!userInfo.isEnabled()) {
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message(NOT_ACTIVE)
                        .status(ResponseObject.ERROR_STATUS).build(), HttpStatus.BAD_REQUEST);
            }

            if (!interviewSessionService.isAlready(interviewersessionID)) {
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message(INTERVIEW_SESSION)
                        .status(ResponseObject.ERROR_STATUS).build(), HttpStatus.BAD_REQUEST);
            }

            Report report = Report.builder()
                    .reportName(reportRequest.getReportName())
                    .reportData(reportRequest.getReportData())
                    .sessionID(interviewersessionID)
                    .createDate(Date.valueOf(LocalDate.now()))
                    .updateDate(Date.valueOf(LocalDate.now())).build();

            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .status(ResponseObject.SUCCESS_STATUS)
                    .message(reportService.reportInterviewSession(report)).build(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .message(e.getMessage())
                    .status(ResponseObject.ERROR_STATUS).build(), HttpStatus.NOT_IMPLEMENTED);
        }
    }


    //get userInterviewer info
    @GetMapping("/profile")
    public ResponseEntity<ResponseObject> getDetailUserInterviewer(HttpServletRequest request) throws JSONException, UserException {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);

            if (!userInfo.isEnabled()) {
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message(NOT_ACTIVE)
                        .status(ResponseObject.ERROR_STATUS).build(), HttpStatus.BAD_REQUEST);
            }

            Integer userId = userInfo.getUserId();
            User user = userService.getUserById(userId);

            Map<String, String> data = new HashMap<>();
            data.put("username", user.getUsername());
            data.put("email", user.getEmail());
            data.put("phonenumber", user.getPhoneNumber());
            data.put("roleName", user.getRoleName().name());

            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .status(ResponseObject.SUCCESS_STATUS)
                    .data(data).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .message(e.getMessage())
                    .status(ResponseObject.ERROR_STATUS).build(), HttpStatus.NOT_IMPLEMENTED);
        }
    }

    //update userInterviewer
    @PutMapping("/profile")
    public ResponseEntity<ResponseObject> updateUserInterviewer(HttpServletRequest request,
                                                                @RequestBody @Valid UserRequest user0) throws JSONException, UserException {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);

            if (!userInfo.isEnabled()) {
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message(NOT_ACTIVE)
                        .status(ResponseObject.ERROR_STATUS).build(), HttpStatus.BAD_REQUEST);
            }

            Integer userId = userInfo.getUserId();
            User user = userService.updateUser(user0, userId);

            Map<String, Object> data = new HashMap<>();
            data.put("username", user.getUsername());
            data.put("email", user.getEmail());
            data.put("phoneNumber", user.getPhoneNumber());

            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .status(ResponseObject.SUCCESS_STATUS)
                    .data(data).build(), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .status(ResponseObject.ERROR_STATUS)
                    .message(exception.getMessage()).build(), HttpStatus.OK);
        }
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<ResponseObject> getCandidateInformation(@PathVariable("candidateId") Integer candidateId, HttpServletRequest request) throws JSONException, UserException {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);

            if (!userInfo.isEnabled()) {
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message(NOT_ACTIVE)
                        .status(ResponseObject.ERROR_STATUS).build(), HttpStatus.BAD_REQUEST);
            }

            User user = userService.getUserById(candidateId);
            if (!user.getRoleName().equals(Role.Candidate)) {
                throw new UserException(NOT_CANDIDATE);
            }

            Map<String, String> data = new HashMap<>();
            data.put("username", user.getUsername());
            data.put("email", user.getEmail());
            data.put("phonenumber", user.getPhoneNumber());
            data.put("roleName", user.getRoleName().name());

            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .status(ResponseObject.SUCCESS_STATUS)
                    .data(data).build(), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .status(ResponseObject.ERROR_STATUS)
                    .message(exception.getMessage()).build(), HttpStatus.OK);
        }
    }


}
