package com.example.eSmartRecruit.controllers.candidate;
import java.util.LinkedHashMap;
import java.util.Map;

import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.controllers.request_reponse.request.UserRequest;
import com.example.eSmartRecruit.exception.PositionException;
import com.example.eSmartRecruit.exception.UserException;
import com.example.eSmartRecruit.models.Application;

import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.models.User;


import com.example.eSmartRecruit.repositories.ApplicationRepos;
import com.example.eSmartRecruit.services.impl.ApplicationService;
import com.example.eSmartRecruit.services.IStorageService;
import com.example.eSmartRecruit.services.impl.PositionService;
import com.example.eSmartRecruit.services.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.example.eSmartRecruit.controllers.request_reponse.ResponseObject.*;

@RestController
@RequestMapping("eSmartRecruit/candidate")
@AllArgsConstructor
public class CandidateController {
    private PositionService positionService;
    private ApplicationService applicationService;
    private ApplicationRepos applicationRepository;
    private UserService userService;
    private IStorageService storageService;


    @GetMapping("/home")
    public ResponseEntity<ResponseObject> home() {
        try {
            List<Position> data = positionService.getAllPosition();
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status(SUCCESS_STATUS).data(data).message(LIST_SUCCESS).build(), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status(ERROR_STATUS).message(exception.getMessage()).build(), HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @GetMapping("/position/{positionID}")
    ResponseEntity<ResponseObject> getDetailPosition(@PathVariable("positionID") Integer id) {
        try {
            Position pos = positionService.getSelectedPosition(id);
            if (pos == null) {
                return new ResponseEntity<ResponseObject>(ResponseObject.builder().status(ERROR_STATUS).message(NOT_FOUND).build(), HttpStatus.OK);
            }
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status(SUCCESS_STATUS).data(pos).build(), HttpStatus.OK);

        } catch (Exception exception) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status(ERROR_STATUS).message(exception.getMessage()).build(), HttpStatus.OK);
        }
    }


    @PostMapping("/application/create/{positionID}")
    ResponseEntity<ResponseObject> applyForPosition(@PathVariable("positionID") Integer id, HttpServletRequest request, @RequestParam("cv") MultipartFile cv) {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            if (!userInfo.isEnabled()) {
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message(NOT_ACTIVE).status(ERROR_STATUS).build(), HttpStatus.BAD_REQUEST);
            }

            String generatedFileName = storageService.storeFile(cv);
            int candidateId = userInfo.getUserId();
            if (!positionService.isPresent(id)) {
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message(NOT_OPEN).status(ERROR_STATUS).build(), HttpStatus.BAD_REQUEST);
            }
            Application application = new Application(candidateId, id, generatedFileName);

            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .message(applicationService.apply(application)).status(SUCCESS_STATUS).build(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().message(e.getMessage()).status(ERROR_STATUS).build(), HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @GetMapping("/application")
    public ResponseEntity<ResponseObject> getMyApplications(HttpServletRequest request) throws JSONException, PositionException, UserException {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            if (!userInfo.isEnabled()) {
                return new ResponseEntity<>(ResponseObject.builder()
                        .status(ResponseObject.ERROR_STATUS)
                        .message(NOT_ACTIVE)
                        .build(), HttpStatus.BAD_REQUEST);
            }
            Integer userId = userInfo.getUserId();
            User user = userService.getUserById(userId);

            List<Application> applications = applicationRepository.findByCandidateID(user.getId());
            List<Map<String, Object>> applicationList = new ArrayList<>();

            for (Application app : applications) {
                Map<String, Object> applicationMap = new LinkedHashMap<>();
                applicationMap.put("applicationID", app.getId());
                applicationMap.put("positionTitle", positionService.getSelectedPosition(app.getPositionID()).getTitle());
                applicationMap.put("status", app.getStatus());
                applicationMap.put("applicationDate", app.getCreateDate().toString());
                applicationList.add(applicationMap);
            }
            return new ResponseEntity<>(ResponseObject.builder()
                    .status(ResponseObject.SUCCESS_STATUS)
                    .data(applicationList)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseObject.builder()
                    .status(ResponseObject.ERROR_STATUS)
                    .message(e.getMessage())
                    .build(), HttpStatus.NOT_IMPLEMENTED);
        }
    }


    @GetMapping("/application/{applicationID}")
    public ResponseEntity<ResponseObject> getApplicationDetails(@PathVariable("applicationID") Integer id, HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            if (!userInfo.isEnabled()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            Integer userId = userInfo.getUserId();
            User user = userService.getUserById(userId);

            Optional<Application> application = applicationRepository.findById(id);
            if (application.isPresent()) {
                Map<String, Object> data = new LinkedHashMap<>();
                Application app = application.get();

                data.put("applicationID", app.getId());
                data.put("candidateName", user.getUsername());
                data.put("positionTitle", positionService.getSelectedPosition(app.getPositionID()).getTitle());
                data.put("status", app.getStatus());
                data.put("cv", app.getCv());
                data.put("applicationDate", app.getCreateDate().toString());

                return ResponseEntity.ok(ResponseObject.builder().status(ResponseObject.SUCCESS_STATUS).data(data).build());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseObject.builder().status(ResponseObject.ERROR_STATUS).build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder().message(e.getMessage()).status(ResponseObject.ERROR_STATUS).build());
        }
    }

    //get user info
    @GetMapping("/profile")
    public ResponseEntity<ResponseObject> getDetailUser(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            if(!userInfo.isEnabled()){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            Integer userId = userInfo.getUserId();
            User user = userService.getUserById(userId);

            Map<String, String> data = new LinkedHashMap<>();
            data.put("username", user.getUsername());
            data.put("email", user.getEmail());
            data.put("phonenumber", user.getPhoneNumber());

            return ResponseEntity.ok(ResponseObject.builder().status(ResponseObject.SUCCESS_STATUS).message(LOAD_SUCCESS).data(data).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder().status(ResponseObject.ERROR_STATUS).message(e.getMessage()).build());
        }
    }

    //update user
    @PutMapping("/profile")
    public ResponseEntity<ResponseObject> updateUser(HttpServletRequest request,
                                                     @RequestBody @Valid UserRequest userRequest) {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);

            if (!userInfo.isEnabled()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Integer userId = userInfo.getUserId();
            User user = userService.updateUser(userRequest, userId);

            if (user != null) {
                Map<String, Object> data = new LinkedHashMap<>();
                data.put("email", user.getEmail());
                data.put("phoneNumber", user.getPhoneNumber());

                return ResponseEntity.ok(ResponseObject.builder().status(ResponseObject.SUCCESS_STATUS)
                        .message(UPDATED_SUCCESS).data(data).build());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ResponseObject.builder().status(ResponseObject.ERROR_STATUS)
                                .message(UPDATED_FAIL).build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseObject.builder().status(ResponseObject.ERROR_STATUS)
                            .message(e.getMessage()).build());
        }
    }


    @PutMapping("/application/{applicationID}")
    public ResponseEntity<ResponseObject> updateApplyPosition(@PathVariable("applicationID") Integer id,
                                                              HttpServletRequest request,
                                                              @RequestParam("cv") MultipartFile cv) {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);

            if (!userInfo.isEnabled()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseObject.builder()
                        .message(NOT_ACTIVE).status(ResponseObject.ERROR_STATUS).build());
            }

            String generatedFileName = storageService.storeFile(cv);
            int candidateId = userInfo.getUserId();
            Application application = new Application(generatedFileName);

            String message = applicationService.update(candidateId, application, id);

            if ("SUCCESS".equals(message)) {
                return ResponseEntity.ok(ResponseObject.builder().message(message).status(ResponseObject.SUCCESS_STATUS).build());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                        .message(message).status(ResponseObject.ERROR_STATUS).build());
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                    .message(e.getMessage()).status(ResponseObject.ERROR_STATUS).build());
        }
    }


    @DeleteMapping("/application/{applicationID}")
    public ResponseEntity<ResponseObject> deleteApplyPosition(@PathVariable("applicationID") Integer id,
                                                              HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);

            if (!userInfo.isEnabled()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseObject.builder()
                        .message(NOT_ACTIVE).status(ResponseObject.ERROR_STATUS).build());
            }

            Integer candidateId = userInfo.getUserId();
            String message = applicationService.deletejob(candidateId, id);

            if ("SUCCESS".equals(message)) {
                return ResponseEntity.ok(ResponseObject.builder().message(message).status(ResponseObject.SUCCESS_STATUS).build());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                        .message(message).status(ResponseObject.ERROR_STATUS).build());
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                    .message(e.getMessage()).status(ResponseObject.ERROR_STATUS).build());
        }
    }


}
