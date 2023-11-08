package com.example.eSmartRecruit.controllers.admin;

import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.services.impl.ApplicationService;
import com.example.eSmartRecruit.services.impl.InterviewSessionService;
import com.example.eSmartRecruit.services.impl.PositionService;
import com.example.eSmartRecruit.services.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("eSmartRecruit/admin")
public class AdminController {
  @Autowired
    private InterviewSessionService interviewSessionService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private UserService userService;
    @Autowired
    private Validator validator;


    public AdminController(UserService userService, Validator validator) {
        this.userService = userService;
        this.validator=validator;

    }

    @GetMapping("/home")
    public ResponseEntity<ResponseObject> home(HttpServletRequest request) throws JSONException {

            String authHeader = request.getHeader("Authorization");
            //return new ResponseEntity<String>("hello",HttpStatus.OK);
            ExtractUser userInfo = new ExtractUser(authHeader, userService);
            if (!userInfo.isEnabled()) {
                return null;
            }
//            Integer userId = userInfo.getUserId();
//            User user = userService.getUserById(userId);

            Map<String, Object> homeList = new LinkedHashMap<>();
            homeList.put("no_user", userService.getcountUser() );
            homeList.put("no_position", positionService.getcountPosition());
            homeList.put("no_application", applicationService.getcountApplication());
            homeList.put("no_interview_session",interviewSessionService.getCountInterview() );
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("Success").data(homeList).build(), HttpStatus.OK);

    }
    @PostMapping("/position/create")
    public ResponseEntity<ResponseObject> createPost(@RequestBody Position position, BindingResult bindingResult, HttpServletRequest request)throws JSONException {
        // Kiểm tra tính hợp lệ của đối tượng Position
        validator.validate(position, bindingResult);

        String authHeader = request.getHeader("Authorization");
        //return new ResponseEntity<String>("hello",HttpStatus.OK);
        ExtractUser userInfo = new ExtractUser(authHeader, userService);
        if (!userInfo.isEnabled()) {
            return null;
        }
        System.out.println("đã chạy create post");
        Position createPosition = positionService.createPost(position);
        Map<String, Object> datapost = new LinkedHashMap<>();
        datapost.put("title",createPosition.getTitle());
        datapost.put("jobDescription",createPosition.getJobDescription());
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
}
