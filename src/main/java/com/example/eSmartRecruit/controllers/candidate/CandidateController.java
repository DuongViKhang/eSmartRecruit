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


@RestController
@RequestMapping("eSmartRecruit/")
@AllArgsConstructor
public class CandidateController {
    private PositionService positionService;
    private ApplicationService applicationService;
    private ApplicationRepos applicationRepository;

    private UserService userService;
    private IStorageService storageService;


    @GetMapping("/home")
    public ResponseEntity<ResponseObject> home()
    {
        try{
            List<Position> data = positionService.getAllPosition();
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("SUCCESS").data(data).message("List position successfully!").build(), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("ERROR").message(exception.getMessage()).build(),HttpStatus.NOT_IMPLEMENTED);
        }
    }
    @GetMapping("/position/{positionID}")
    ResponseEntity<ResponseObject> getDetailPosition(@PathVariable("positionID")Integer id){
        try{
            Position pos = positionService.getSelectedPosition(id);
            if(pos == null){
                return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("ERROR").message("Position not found").build(),HttpStatus.OK);
            }
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("SUCCESS").data(pos).build(),HttpStatus.OK);

        }catch (Exception exception){
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("ERROR").message(exception.getMessage()).build(),HttpStatus.OK);
        }
    }

    @PostMapping("/application/create/{positionID}")
    ResponseEntity<ResponseObject> applyForPosition(@PathVariable("positionID")Integer id, HttpServletRequest request, @RequestParam("cv")MultipartFile cv){
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            if(!userInfo.isEnabled()){
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message("Account not active!").status("ERROR").build(),HttpStatus.BAD_REQUEST);
            }

            String generatedFileName = storageService.storeFile(cv);
            int candidateId = userInfo.getUserId();
            if(!positionService.isPresent(id)){
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message("Position not open!").status("ERROR").build(),HttpStatus.BAD_REQUEST);
            }
            Application application = new Application(candidateId, id, generatedFileName);

            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .message(applicationService.apply(application)).status("SUCCESS").build(),HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().message(e.getMessage()).status("ERROR").build(),HttpStatus.NOT_IMPLEMENTED);
        }
    }
    @GetMapping("/application")
    public ResponseEntity<ResponseObject> getMyApplications(HttpServletRequest request) throws JSONException, PositionException, UserException {
        {
            String authHeader = request.getHeader("Authorization");
            //return new ResponseEntity<String>("hello",HttpStatus.OK);
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            if (!userInfo.isEnabled()) {
                return null;
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
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("Success").data(applicationList).build(), HttpStatus.OK);
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

                return ResponseEntity.ok(ResponseObject.builder().status("SUCCESS").data(data).build());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseObject.builder().status("ERROR").build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder().message(e.getMessage()).status("ERROR").build());
        }
    }
    //get user info
    @GetMapping("/profile")
    ResponseEntity<ResponseObject> getDetailUser(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            if(!userInfo.isEnabled()){
                return null;
            }
            Integer userId = userInfo.getUserId();
            User user = userService.getUserById(userId);

            Map<String, String> data = new LinkedHashMap<>();
            data.put("username", user.getUsername());
            data.put("email", user.getEmail());
            data.put("phonenumber", user.getPhoneNumber());

            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("Success").message("Loading data success!").data(data).build(),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("ERROR").message(e.getMessage()).build(),HttpStatus.NOT_IMPLEMENTED);
        }
    }
    //update user
    @PutMapping("/profile")
    ResponseEntity<ResponseObject> updateUser(HttpServletRequest request,
                                              @RequestBody @Valid UserRequest user0
                                              ) throws JSONException, UserException {
        try {
            String authHeader = request.getHeader("Authorization");
            //return new ResponseEntity<String>("hello",HttpStatus.OK);
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            if (!userInfo.isEnabled()) {
                return null;
            }
            Integer userId = userInfo.getUserId();
            User user = userService.updateUser(user0, userId);
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("email", user.getEmail());
            data.put("phoneNumber", user.getPhoneNumber());

        return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("Success").message("Update information successfully!").data(data).build(),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("ERROR").message(e.getMessage()).build(),HttpStatus.NOT_IMPLEMENTED);

        }
    }

    @PutMapping("/application/{applicationID}")
    ResponseEntity<ResponseObject> updateApplyPosition(@PathVariable("applicationID")Integer id, HttpServletRequest request, @RequestParam("cv")MultipartFile cv){
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            if(!userInfo.isEnabled()){
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message("Account not active!").status("ERROR").build(),HttpStatus.BAD_REQUEST);
            }
            String generatedFileName = storageService.storeFile(cv);
            int candidateId = userInfo.getUserId();

            Application application = new Application(generatedFileName);
            //Application application = new Application(candidateId, id, generatedFileName);

            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .message(applicationService.update(candidateId,application,id)).status("SUCCESS").build(),HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().message(e.getMessage()).status("ERROR").build(),HttpStatus.NOT_IMPLEMENTED);

        }

    }

    @DeleteMapping("/application/{applicationID}")
    ResponseEntity<ResponseObject> deleteApplyPosition(@PathVariable("applicationID")Integer id, HttpServletRequest request){
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            if(!userInfo.isEnabled()){
                return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                        .message("Account not active!").status("ERROR").build(),HttpStatus.BAD_REQUEST);
            }
            Integer candidateId = userInfo.getUserId();

            return new ResponseEntity<ResponseObject>(ResponseObject.builder().message(applicationService.deletejob(candidateId, id)).status("SUCCESS").build(),HttpStatus.OK);


    }catch (Exception e){
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().message("Error").status("ERROR").build(),HttpStatus.NOT_IMPLEMENTED);
        }

        }

    }
