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
import java.util.*;
//

import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;

import com.example.eSmartRecruit.models.Application;
import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.models.Skill;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.repositories.ApplicationRepos;
import com.example.eSmartRecruit.repositories.SkillRepos;
import com.example.eSmartRecruit.services.impl.ApplicationService;
import com.example.eSmartRecruit.services.impl.PositionService;
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
    private ApplicationRepos applicationRepository;
    private PositionService positionService;

    private UserService userService;
    private ApplicationService applicationService;

    private SkillRepos skillRepository;

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
    @GetMapping("/application/{applicationID}")
    ResponseEntity<ResponseObject> getDetailApplication(@PathVariable("applicationID") Integer Id,HttpServletRequest request) throws JSONException {

        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            if (!userInfo.isEnabled()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Optional<Application> application = applicationRepository.findById(Id);


            if (application.isPresent()) {
                Map<String, Object> data = new LinkedHashMap<>();
                Application app = application.get();
                User user = userService.getUserById(app.getCandidateID());
                List<Skill> skillsOb = skillRepository.findByCandidateId(app.getCandidateID());
                List<String> skills = new ArrayList<>();
                for(Skill skill : skillsOb){
                    skills.add(skill.getSkillName().toString());
                }


                data.put("applicationID", app.getId());
                data.put("candidateName", user.getUsername());
                data.put("positionID", app.getPositionID());
                data.put("positionTitle", positionService.getSelectedPosition(app.getPositionID()).getTitle());
                data.put("status", app.getStatus());
                data.put("cv", "https://example.com/cv/");
                data.put("applicationDate", app.getCreateDate().toString());
                data.put("skills", skills);


                return ResponseEntity.ok(ResponseObject.builder().status("SUCCESS").data(data).build());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseObject.builder().status("ERROR").build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder().message(e.getMessage()).status("ERROR").build());
        }

    }
}
