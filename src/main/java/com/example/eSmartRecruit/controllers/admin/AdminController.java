package com.example.eSmartRecruit.controllers.admin;

import com.example.eSmartRecruit.authentication.AuthenticationService;
import com.example.eSmartRecruit.authentication.request_reponse.RegisterRequest;
import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.controllers.request_reponse.request.InterviewSessionRequest;
import com.example.eSmartRecruit.controllers.request_reponse.request.PositionRequest;
import com.example.eSmartRecruit.exception.PositionException;
import com.example.eSmartRecruit.exception.UserException;

import com.example.eSmartRecruit.models.*;
import com.example.eSmartRecruit.repositories.ApplicationRepos;
import com.example.eSmartRecruit.services.impl.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("eSmartRecruit/admin")
public class AdminController {

    private UserService userService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ApplicationRepos applicationRepository;
    private InterviewSessionService interviewSessionService;
    private AuthenticationService authenticationService;
    @Autowired
    private ReportService reportService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/home")
    public ResponseEntity<ResponseObject> home(HttpServletRequest request) throws JSONException, UserException {

        String authHeader = request.getHeader("Authorization");
        //return new ResponseEntity<String>("hello",HttpStatus.OK);
        ExtractUser userInfo = new ExtractUser(authHeader, userService);
        if (!userInfo.isEnabled()) {
            return null;
        }
        Map<String, Object> homeList = new LinkedHashMap<>();
        homeList.put("no_user", userService.getcountUser() );
        homeList.put("no_position", positionService.getcountPosition());
        homeList.put("no_application", applicationService.getcountApplication());
        homeList.put("no_interview_session",interviewSessionService.getCountInterview() );
        return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("Success").data(homeList).build(), HttpStatus.OK);

    }

    @PostMapping("/position/create")
    public ResponseEntity<ResponseObject> createPost(@RequestBody Position position, HttpServletRequest request) throws JSONException, UserException {

        String authHeader = request.getHeader("Authorization");
        //return new ResponseEntity<String>("hello",HttpStatus.OK);
        ExtractUser userInfo = new ExtractUser(authHeader, userService);
        if (!userInfo.isEnabled()) {
            return null;
        }
        // System.out.println("đã chạy create post");
        Position createPosition = positionService.createPost(position);
        Map<String, Object> datapost = new LinkedHashMap<>();
        datapost.put("title", createPosition.getTitle());
        datapost.put("jobDescription", createPosition.getJobDescription());
        datapost.put("jobRequirements", createPosition.getJobRequirements());
        datapost.put("salary", createPosition.getSalary());
        datapost.put("expireDate", createPosition.getExpireDate());
        datapost.put("location", createPosition.getLocation());

        ResponseObject response = ResponseObject.builder()
                .status("Success")
                .data(datapost)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/position")
    public ResponseEntity<ResponseObject> PositionAdmin(HttpServletRequest request) {
        try{
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            Integer userId = userInfo.getUserId();
            User user = userService.getUserById(userId);
            if (!userInfo.isEnabled() || !userService.getUserRole(userId).toLowerCase().equalsIgnoreCase("admin")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            List<Position> data = positionService.getAllPosition();
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("SUCCESS").data(data).message("Loading position successfully").build(), HttpStatus.OK);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder().status("ERROR").message(exception.getMessage()).build());
        }
    }

    @GetMapping("/position/{positionID}")
    ResponseEntity<ResponseObject> getDetailPositionAdmin(@PathVariable("positionID")Integer id,HttpServletRequest request){
        try{
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            Integer userId = userInfo.getUserId();
            User user = userService.getUserById(userId);
            if (!userInfo.isEnabled() || !userService.getUserRole(userId).toLowerCase().equalsIgnoreCase("admin")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Position positions = positionService.getSelectedPosition(id);

            if(positions == null){
                return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("ERROR").message("Position not found").build(),HttpStatus.OK);
            }
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("SUCCESS").message("Loading position successfully").data(positions).build(),HttpStatus.OK);

        }catch (Exception exception){
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("ERROR").message(exception.getMessage()).build(),HttpStatus.OK);
        }
    }

    @GetMapping("/application")
    public ResponseEntity<ResponseObject> getApplications(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            Integer userId = userInfo.getUserId();
            User user = userService.getUserById(userId);
            if (!userInfo.isEnabled() || !userService.getUserRole(userId).toLowerCase().equalsIgnoreCase("admin")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            List<Application> applications = applicationRepository.findAll();
            List<Map<String, Object>> applicationList = new ArrayList<>();

            for (Application app : applications) {
                Map<String, Object> applicationMap = new LinkedHashMap<>();
                applicationMap.put("applicationID", app.getId());
                applicationMap.put("positionTitle", positionService.getSelectedPosition(app.getPositionID()).getTitle());
                applicationMap.put("status", app.getStatus());
                applicationMap.put("applicationDate", app.getCreateDate().toString());
                applicationList.add(applicationMap);
            }
            return ResponseEntity.ok(ResponseObject.builder().status("SUCCESS").message("Applications retrieved successfully.").data(applicationList).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder().status("ERROR").message(e.getMessage()).build());
        }
    }

    @GetMapping("/application/{applicationID}")
    public ResponseEntity<ResponseObject> getApplication(@PathVariable("applicationID") Integer Id, HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            Integer userId = userInfo.getUserId();

            if (!userInfo.isEnabled() || !userService.getUserRole(userId).toLowerCase().equalsIgnoreCase("admin")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Optional<Application> application = applicationRepository.findById(Id);
            if (application.isPresent()) {
                Application app = application.get();
                Map<String, Object> data = new LinkedHashMap<>();

                User user = userService.getUserById(app.getCandidateID());

                data.put("applicationID", app.getId());
                data.put("candidateName", user.getUsername());
                data.put("positionTitle", positionService.getSelectedPosition(app.getPositionID()).getTitle());
                data.put("status", app.getStatus());
                data.put("cv", app.getCv());
                data.put("applicationDate", app.getCreateDate().toString());

                return ResponseEntity.ok(ResponseObject.builder().status("SUCCESS").message("Application").data(data).build());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseObject.builder().status("ERROR").message("Application not found").build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder().status("ERROR").message("Internal server error").build());
        }
    }
    @PutMapping("/position/{positionID}")
    public ResponseEntity<ResponseObject> editPosition(@PathVariable Integer positionID, @RequestBody PositionRequest positionRequest, HttpServletRequest request) throws JSONException, UserException, PositionException {
        String authHeader = request.getHeader("Authorization");
        ExtractUser userInfo = new ExtractUser(authHeader, userService);

        if (!userInfo.isEnabled()) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .message("Account not active!").status("ERROR").build(), HttpStatus.BAD_REQUEST);
        }
        try {
            Position existingPosition = positionService.getSelectedPosition(positionID);
            Position updatedPosition = Position.builder()
                    .title(positionRequest.getTitle())
                    .jobDescription(positionRequest.getJobDescription())
                    .jobRequirements(positionRequest.getJobRequirements())
                    .salary(positionRequest.getSalary())
                    .expireDate(positionRequest.getExpireDate())
                    .location(positionRequest.getLocation())
                    .build();positionService.editPosition(positionID, updatedPosition);

            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .status("SUCCESS").message("Position updated successfully").build(), HttpStatus.OK);
        } catch (PositionException e) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .message("Error editing position: " + e.getMessage()).status("ERROR").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/position/{positionID}")
    public ResponseEntity<ResponseObject> deletePosition(@PathVariable Integer positionID, HttpServletRequest request) throws JSONException, UserException, PositionException {
        String authHeader = request.getHeader("Authorization");
        ExtractUser userInfo = new ExtractUser(authHeader, userService);
        if (!userInfo.isEnabled()) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .message("Account not active!").status("ERROR").build(), HttpStatus.BAD_REQUEST);
        }
        Position existingPosition = positionService.getSelectedPosition(positionID);

        try {
            positionService.deletePosition(positionID);
        } catch (PositionException e) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .message("Error deleting position: " + e.getMessage()).status("ERROR").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                .status("SUCCESS").message("Position deleted successfully").build(), HttpStatus.OK);
    }
    @GetMapping("/user")
    public ResponseEntity<ResponseObject> getUsers(HttpServletRequest request) throws JSONException {
        try {
            String authHeader = request.getHeader("Authorization");
            logger.info("Received request to get users. Authorization header: {}", authHeader);

            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            Integer userId = userInfo.getUserId();
            if (!userInfo.isEnabled() || !userService.getUserRole(userId).equalsIgnoreCase("admin")) {
                logger.warn("Unauthorized access. User ID: {}", userId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            List<User> userList = userService.getAllUser();
            List<Map<String, Object>> dataList = new ArrayList<>();
            for (User user : userList) {
                Map<String, Object> data = new LinkedHashMap<>();
                data.put("id", user.getId());
                data.put("username", user.getUsername());
                data.put("email", user.getEmail());
                data.put("phonenumber", user.getPhoneNumber());
                data.put("rolename", user.getRoleName());
                data.put("status", user.getStatus());
                data.put("create_date", user.getCreateDate());
                data.put("update_date", user.getUpdateDate());
                dataList.add(data);
            }

            logger.info("Returning user list. Total users: {}", userList.size());
            return ResponseEntity.ok(ResponseObject.builder().status("SUCCESS").message("List all users successfully!").data(dataList).build());
        } catch (UserException e) {
            logger.error("Internal Server Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder().status("ERROR").message(e.getMessage()).build());
        }
    }
    @PostMapping("/user/create")
    ResponseEntity<ResponseObject> createUser(HttpServletRequest request,
                                              @RequestBody @Valid RegisterRequest registerRequest) {
        try {
            String authHeader = request.getHeader("Authorization");
            logger.info("Received request to get users. Authorization header: {}", authHeader);

            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            Integer userId = userInfo.getUserId();
            if (!userInfo.isEnabled() || !userService.getUserRole(userId).equalsIgnoreCase("admin")) {
                logger.warn("Unauthorized access. User ID: {}", userId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.ok(userService.saveUser(registerRequest));
        } catch (UserException | JSONException e) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .message(e.getMessage())
                    .status("ERROR").build(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/report/{interviewsessionid}")
    public ResponseEntity<ResponseObject> getReport(@PathVariable("interviewsessionid") Integer sessionId, HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            Integer userId = userInfo.getUserId();

            if (!userInfo.isEnabled() ) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Report reportObject = reportService.getReportBySessionId(sessionId); // Sử dụng sessionId thay vì userId
            if (reportObject != null) {

                Map<String, Object> data = new LinkedHashMap<>();
                data.put("id", reportObject.getId());
                data.put("report_name", reportObject.getReportName());
                data.put("report_data", reportObject.getReportData());
                data.put("createDate", reportObject.getCreateDate() != null ? reportObject.getCreateDate().toString() : null);
                data.put("updateDate", reportObject.getUpdateDate() != null ? reportObject.getUpdateDate().toString() : null);

                return ResponseEntity.ok(ResponseObject.builder().status("SUCCESS").message("Report").data(data).build());
            }
        else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseObject.builder().status("ERROR").message("Report not found").build());
            }
        } catch (Exception e) {
            logger.error("Error in getReport", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder().status("ERROR").message("Internal server error").build());
        }
    }
    @PutMapping ("/interviewsession/{interviewsessionid}")
    public ResponseEntity<ResponseObject> scheduleInterview(@PathVariable("interviewsessionid") Integer id, HttpServletRequest request,
                                                            @RequestBody @Valid InterviewSessionRequest interviewSessionRequest) {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);

            if (!userInfo.isEnabled()) {
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message("Account not active!").status("ERROR").build(), HttpStatus.BAD_REQUEST);
            }

            interviewSessionService.scheduleInterview(id, interviewSessionRequest);

            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .message("Schedule successfully!").status("SUCCESS").data(interviewSessionService.scheduleInterview(id, interviewSessionRequest)).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().message(e.getMessage()).status("ERROR").build(), HttpStatus.NOT_IMPLEMENTED);
        }
    }
}
