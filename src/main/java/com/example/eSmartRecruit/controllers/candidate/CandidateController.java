package com.example.eSmartRecruit.controllers.candidate;

import com.example.eSmartRecruit.models.Applications;
import com.example.eSmartRecruit.models.Positions;
import com.example.eSmartRecruit.service.impl.ApplicationService;
import com.example.eSmartRecruit.service.IStorageService;
import com.example.eSmartRecruit.service.impl.PositionService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    ResponseEntity<Positions> getDetailPosition(@PathVariable("positionID")Integer id){
        Positions pos = positionService.getSelectedPosition(id);
        //return new ResponseEntity<String>("hello",HttpStatus.OK);
        return new ResponseEntity<Positions>(pos,HttpStatus.OK);

        //return new ResponseEntity<Positions>(positionService.getSelectedPosition(id),HttpStatus.OK);
    }



    //Đợi session
    //Để candidateID mặc định 1
    @PostMapping("/application/create/{positionID}")
    ResponseEntity<String> applyForPosition(@PathVariable("positionID")Integer id, HttpServletRequest request, @RequestParam("file")MultipartFile file){
        try {
            //
            String authHeader = request.getHeader("Authorization");
            System.out.println(authHeader);

            //

            String generatedFileName = storageService.storeFile(file);
            Integer candidateId = 1;
            Applications applications = new Applications(candidateId, id,"PENDING", generatedFileName, Date.valueOf(LocalDate.now()));

            return new ResponseEntity<String>(applicationService.apply(applications),HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_IMPLEMENTED);
        }

    }
}
