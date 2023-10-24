package com.example.eSmartRecruit.controllers.candidate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("eSmartRecruit/")
public class CandidateController {
    @GetMapping("/home")
    List<String> getAllCandidate(){
        return List.of("Hello candidate");
    }
}
