package com.example.eSmartRecruit.controllers.admin;

import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.controllers.request_reponse.request.PositionRequest;
import com.example.eSmartRecruit.exception.PositionException;
import com.example.eSmartRecruit.exception.UserException;

import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.models.Application;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.repositories.ApplicationRepos;
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
}
