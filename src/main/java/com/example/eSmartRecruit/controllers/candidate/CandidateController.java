package com.example.eSmartRecruit.controllers.candidate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.repositories.UserRepos;

import java.util.List;

@RestController
@RequestMapping("eSmartRecruit/")
public class CandidateController {
	
	@Autowired
	UserRepos userRepos;
    @GetMapping("/home")
    List<User> getAll(){
    	return userRepos.findAll();
    }
}
