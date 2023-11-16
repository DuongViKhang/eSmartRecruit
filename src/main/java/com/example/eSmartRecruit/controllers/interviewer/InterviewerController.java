package com.example.eSmartRecruit.controllers.interviewer;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

@AllArgsConstructor
@RestController
@RequestMapping("eSmartRecruit/interviewer")
public class InterviewerController {
    private UserService userService;
    private InterviewSessionService interviewSessionService;
    private ReportService reportService;

    @GetMapping("/home")
    ResponseEntity<ResponseObject> getInterviewerSession(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);

            Integer interviewerId = userInfo.getUserId();
            List<InterviewSession> interviewSessionList = interviewSessionService.findByInterviewerID(interviewerId);

            // trường hợp danh sách phiên phỏng vấn trống
            if (interviewSessionList.isEmpty()) {
                return new ResponseEntity<>(ResponseObject.builder()
                        .message(ResponseObject.NO_INTERVIEWSESSION).status(ResponseObject.SUCCESS_STATUS)
                        .data(Collections.emptyList()).build(), HttpStatus.OK);
            }
            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .message("").status(ResponseObject.SUCCESS_STATUS)
                    .data(interviewSessionList).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .message(e.getMessage())
                    .status(ResponseObject.ERROR_STATUS).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{interviewersessionID}")
    ResponseEntity<ResponseObject> findByInterviewSessionID(@PathVariable("interviewersessionID") Integer interviewersessionID, HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);

            InterviewSession interviewSession = interviewSessionService.findByID(interviewersessionID);
            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .message("").status(ResponseObject.SUCCESS_STATUS)
                    .data(interviewSession).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .message(e.getMessage())
                    .status(ResponseObject.ERROR_STATUS).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/report/create/{interviewersessionID}")
    ResponseEntity<ResponseObject> reportInterviewSession(@PathVariable("interviewersessionID") Integer interviewersessionID,
                                                          HttpServletRequest request, @RequestBody @Valid ReportRequest reportRequest) {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);


            if (!interviewSessionService.isAlready(interviewersessionID)) {
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message(ResponseObject.INTERVIEW_SESSION)
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
                    .status(ResponseObject.ERROR_STATUS).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    //get userInterviewer info
    @GetMapping("/profile")
    ResponseEntity<ResponseObject> getDetailUserInterviewer(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);


            Integer userId = userInfo.getUserId();
            User user = userService.getUserById(userId);

            Map<String, String> data = new HashMap<>();
            data.put("username", user.getUsername());
            data.put("email", user.getEmail());
            data.put("phonenumber", user.getPhoneNumber());
            data.put("roleName", user.getRoleName().name());

            return new ResponseEntity<>(ResponseObject.builder().status(ResponseObject.SUCCESS_STATUS).data(data).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseObject.builder().status(ResponseObject.ERROR_STATUS)
                    .message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //update userInterviewer
    @PutMapping("/profile")
    ResponseEntity<ResponseObject> updateUserInterviewer(HttpServletRequest request,
                                                         @RequestBody @Valid UserRequest user0) {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            if (!userInfo.isEnabled()) {
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message("Account not active!").status("ERROR").build(), HttpStatus.BAD_REQUEST);
            }
            Integer userId = userInfo.getUserId();
            User user = userService.updateUser(user0, userId);
            Map<String, Object> data = new HashMap<>();
            data.put("username", user.getUsername());
            data.put("email", user.getEmail());
            data.put("phoneNumber", user.getPhoneNumber());
            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .status(ResponseObject.SUCCESS_STATUS).data(data).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .status(ResponseObject.ERROR_STATUS).message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/candidate/{candidateId}")
    ResponseEntity<ResponseObject> getCandidateInformation(@PathVariable("candidateId") Integer candidateId, HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);


            User candidate = userService.getUserById(candidateId);
            if (!candidate.getRoleName().equals(Role.Candidate)) {
                return new ResponseEntity<>(ResponseObject.builder()
                        .message(ResponseObject.NOT_CANDIDATE)
                        .status(ResponseObject.ERROR_STATUS).build(), HttpStatus.BAD_REQUEST);
            }

            Map<String, String> data = new HashMap<>();
            data.put("username", candidate.getUsername());
            data.put("email", candidate.getEmail());
            data.put("phonenumber", candidate.getPhoneNumber());
            data.put("roleName", candidate.getRoleName().name());

            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status(ResponseObject.SUCCESS_STATUS).data(data).build(), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status(ResponseObject.ERROR_STATUS)
                    .message(exception.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}