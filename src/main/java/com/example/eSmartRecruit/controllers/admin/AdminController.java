package com.example.eSmartRecruit.controllers.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("eSmartRecruit/admin")
public class AdminController {
    @GetMapping("/home")
    List<String> getAllAdmin(){
        return List.of("Hello admin");
    }
}
