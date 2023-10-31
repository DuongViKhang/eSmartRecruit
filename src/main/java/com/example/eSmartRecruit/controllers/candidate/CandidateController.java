package com.example.eSmartRecruit.controllers.candidate;

import com.example.eSmartRecruit.models.Application;
import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.service.impl.ApplicationService;
import com.example.eSmartRecruit.service.IStorageService;
import com.example.eSmartRecruit.service.impl.PositionService;
//import com.example.eSmartRecruit.repositories.PositionRepos;
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
    //  private PositionRepos positionRepository;

    @GetMapping("/home")
    public List<Position> getAllPositions() {
        return positionService.getAllPositions();
    }

    @GetMapping("/position/{positionID}")
    public ResponseEntity<Position> getDetailPosition(@PathVariable("positionID") Integer id) {
        Position pos = positionService.getSelectedPosition(id);
        return new ResponseEntity<>(pos, HttpStatus.OK);
    }

    @PostMapping("/application/create/{positionID}")
    public ResponseEntity<String> applyForPosition(@PathVariable("positionID") Integer id, HttpServletRequest request,
                                                   @RequestParam("cv") MultipartFile cv,
                                                   @RequestParam("updateDate") Date updateDate) {
        try {
            String authHeader = request.getHeader("Authorization");
            System.out.println(authHeader);

            String generatedFileName = storageService.storeFile(cv);
            int candidateId = 1;
            Application application = new Application(candidateId, id, generatedFileName, updateDate);

            return new ResponseEntity<>(applicationService.apply(application), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @GetMapping("/positions")
    public List<Position> searchPositions(@RequestParam("keyword") String keyword) {
        return positionService.searchPositions(keyword);
    }
}