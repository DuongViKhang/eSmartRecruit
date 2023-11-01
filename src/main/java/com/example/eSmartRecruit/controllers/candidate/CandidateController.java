package com.example.eSmartRecruit.controllers.candidate;


import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.controllers.request_reponse.CandidateApplyResponse;
import com.example.eSmartRecruit.controllers.request_reponse.OnePositionResponse;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.models.Application;

import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.services.impl.ApplicationService;
import com.example.eSmartRecruit.services.IStorageService;
import com.example.eSmartRecruit.services.impl.PositionService;
import com.example.eSmartRecruit.services.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.repositories.PositionRepos;
import com.example.eSmartRecruit.services.impl.ApplicationService;
import com.example.eSmartRecruit.services.IStorageService;
import com.example.eSmartRecruit.services.impl.PositionService;
import jakarta.servlet.http.HttpServletRequest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("eSmartRecruit/")
@AllArgsConstructor
public class CandidateController {
    private PositionService positionService;
    private ApplicationService applicationService;

    private UserService userService;
    private IStorageService storageService;
    @GetMapping("/home")
    List<String> getAllCandidate(){
        return List.of("Hello candidate","");
    }

    @GetMapping("/position/{positionID}")
    ResponseEntity<ResponseObject> getDetailPosition(@PathVariable("positionID")Integer id){
        Position pos = positionService.getSelectedPosition(id);
        //return new ResponseEntity<String>("hello",HttpStatus.OK);
        return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("SUCCESS").data(pos).build(),HttpStatus.OK);


        //return new ResponseEntity<Positions>(positionService.getSelectedPosition(id),HttpStatus.OK);
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

            Application application = new Application(candidateId, id, generatedFileName);

            return new ResponseEntity<ResponseObject>(ResponseObject.builder()
                    .message(applicationService.apply(application)).status("SUCCESS").build(),HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().message(e.getMessage()).status("ERROR").build(),HttpStatus.NOT_IMPLEMENTED);

        }

    }
}
