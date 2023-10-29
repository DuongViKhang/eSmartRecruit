package com.example.eSmartRecruit.controllers.candidate;

import com.example.eSmartRecruit.models.Application;

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
    private IStorageService storageService;
    private PositionRepos positionRepository;
    @GetMapping("/home")
    List<Position> getAllCandidate(){
        return positionRepository.findAll();
    }

    @GetMapping("/position/{positionID}")
    ResponseEntity<Position> getDetailPosition(@PathVariable("positionID")Integer id){
        Position pos = positionService.getSelectedPosition(id);
        //return new ResponseEntity<String>("hello",HttpStatus.OK);
        return new ResponseEntity<Position>(pos,HttpStatus.OK);

        //return new ResponseEntity<Positions>(positionService.getSelectedPosition(id),HttpStatus.OK);
    }




    //Để candidateID mặc định 1
    @PostMapping("/application/create/{positionID}")
    ResponseEntity<String> applyForPosition(@PathVariable("positionID")Integer id, HttpServletRequest request, @RequestParam("cv")MultipartFile cv,
                                                                                                                @RequestParam("updateDate")Date updateDate){
        try {
            //
            String authHeader = request.getHeader("Authorization");
            System.out.println(authHeader);

            //

            String generatedFileName = storageService.storeFile(cv);
            int candidateId = 1;
            Application application = new Application(candidateId, id, generatedFileName, updateDate);

            return new ResponseEntity<String>(applicationService.apply(application),HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_IMPLEMENTED);
        }

    }
}
