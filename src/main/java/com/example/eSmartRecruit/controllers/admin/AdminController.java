package com.example.eSmartRecruit.controllers.admin;

import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;

import com.example.eSmartRecruit.controllers.request_reponse.request.ApplicationResultRequest;
import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.models.Application;
import com.example.eSmartRecruit.exception.UserException;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.models.enumModel.Role;
import com.example.eSmartRecruit.repositories.ApplicationRepos;
import com.example.eSmartRecruit.services.IStorageService;
import com.example.eSmartRecruit.services.impl.ApplicationService;
import com.example.eSmartRecruit.services.impl.PositionService;
import com.example.eSmartRecruit.services.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.eSmartRecruit.models.enumModel.ApplicationStatus;

import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("eSmartRecruit/admin")
public class AdminController {
    private IStorageService storageService;
    private UserService userService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ApplicationRepos applicationRepository;

    @GetMapping("/position")
    public ResponseEntity<ResponseObject> PositionAdmin()
    {
        try{
            List<Position> data = positionService.getAllPosition();
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("SUCCESS").data(data).message("Loading position successfully").build(), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("ERROR").message(exception.getMessage()).build(),HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @GetMapping("/position/{positionID}")
    ResponseEntity<ResponseObject> getDetailPositionAdmin(@PathVariable("positionID")Integer id){
        try{
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
    @GetMapping("/candidate/{candidateId}")
    ResponseEntity<ResponseObject> getCandidateInformation(@PathVariable("candidateId") Integer candidateId, HttpServletRequest request) throws JSONException, UserException {
        try {
            String authHeader = request.getHeader("Authorization");
            //return new ResponseEntity<String>("hello",HttpStatus.OK);
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            if (!userInfo.isEnabled()) {
                return null;
            }

            User user = userService.getUserById(candidateId);
            if (!user.getRoleName().equals(Role.Candidate)) {
                throw new UserException("Not a candidate");
            }
            Map<String, String> data = new LinkedHashMap<>();
            data.put("username", user.getUsername());
            data.put("email", user.getEmail());
            data.put("phonenumber", user.getPhoneNumber());
            data.put("roleName", user.getRoleName().name());

            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("Success").data(data).build(), HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("Success").message(exception.getMessage()).build(), HttpStatus.OK);
        }
    }
    @PutMapping ("/application/{applicationID}")
    public ResponseEntity<ResponseObject> updateApplicationStatus(@PathVariable("applicationID") Integer id, HttpServletRequest request, @RequestBody ApplicationResultRequest applicationResultRequest) {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);

            if (!userInfo.isEnabled()) {
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message("Account not active!").status("ERROR").build(), HttpStatus.BAD_REQUEST);
            }

            try {
                ApplicationStatus.valueOf(applicationResultRequest.getStatus());
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message("Invalid status").status("ERROR").build(), HttpStatus.BAD_REQUEST);
            }

            ApplicationStatus status1 = ApplicationStatus.valueOf(applicationResultRequest.getStatus());

            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                 .message(applicationService.adminUpdate(id, status1)).status("SUCCESS").data(applicationService.findById(id)).build(), HttpStatus.OK);

//            if (status1 == ApplicationStatus.Approved) {
//                // Xử lý hồ sơ được duyệt
//                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
//                        .message(applicationService.adminUpdate(id, status1)).status("SUCCESS").data(applicationService.findById(id)).build(), HttpStatus.OK);
//            }
//            else if (status1 == ApplicationStatus.Declined) {
//                // Xử lý hồ sơ bị từ chối
//                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
//                        .message(applicationService.adminUpdate(id, status1)).status("SUCCESS").data(applicationService.findById(id)).build(), HttpStatus.OK);
//            } else {
//                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
//                        .message("Invalid status").status("ERROR").build(), HttpStatus.BAD_REQUEST);
//            }
        } catch (Exception e) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().message(e.getMessage()).status("ERROR").build(), HttpStatus.NOT_IMPLEMENTED);
        }
    }

}
