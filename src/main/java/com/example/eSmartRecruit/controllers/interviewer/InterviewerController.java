package com.example.eSmartRecruit.controllers.interviewer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("eSmartRecruit/interviewer")
public class InterviewerController {
    @GetMapping("/home")
    List<String> getAllInterviewer(){
        return List.of("Hello interviewer");
    }
}
