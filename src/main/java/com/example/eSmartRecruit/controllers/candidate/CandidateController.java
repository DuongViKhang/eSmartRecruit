package com.example.eSmartRecruit.controllers.candidate;

import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.models.Application;

import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.models.enumModel.Role;
import com.example.eSmartRecruit.service.impl.ApplicationService;
import com.example.eSmartRecruit.service.IStorageService;
import com.example.eSmartRecruit.service.impl.PositionService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("eSmartRecruit/")
@AllArgsConstructor
public class CandidateController {
    private PositionService positionService;
    private ApplicationService applicationService;
    private IStorageService storageService;
    @GetMapping("/home")
    List<String> getAllCandidate(){
        return List.of("Hello candidate","");
    }

    @GetMapping("/position/{positionID}")
    ResponseEntity<Position> getDetailPosition(@PathVariable("positionID")Integer id){
        Position pos = positionService.getSelectedPosition(id);
        //return new ResponseEntity<String>("hello",HttpStatus.OK);
        return new ResponseEntity<Position>(pos,HttpStatus.OK);

        //return new ResponseEntity<Positions>(positionService.getSelectedPosition(id),HttpStatus.OK);
    }

    @PostMapping("/application/create/{positionID}")
    ResponseEntity<String> applyForPosition(@PathVariable("positionID")Integer id, HttpServletRequest request, @RequestParam("cv")MultipartFile cv){
        try {
            String authHeader = request.getHeader("Authorization");
            ExtractUser userInfo = new ExtractUser(authHeader);
            if(userInfo.getUserRole()!= Role.Candidate){
                return new ResponseEntity<String>("Not a candidate",HttpStatus.BAD_REQUEST);
            }

            String generatedFileName = storageService.storeFile(cv);
            int candidateId = userInfo.getUserId();

            Application application = new Application(candidateId, id, generatedFileName);

            return new ResponseEntity<String>(applicationService.apply(application),HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_IMPLEMENTED);
        }

    }
}
