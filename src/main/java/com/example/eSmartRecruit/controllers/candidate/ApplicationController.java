package com.example.eSmartRecruit.controllers.candidate;

import com.example.eSmartRecruit.models.Application;

import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.repositories.ApplicationRepos;
import com.example.eSmartRecruit.services.impl.PositionService;
import com.example.eSmartRecruit.services.impl.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/application")
public class ApplicationController {
    @Autowired
    private ApplicationRepos applicationRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private PositionService positionService;

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getMyApplications(@RequestParam String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            List<Application> applications = applicationRepository.findByCandidateID(user.get().getId());
            List<Map<String, Object>> applicationList = new ArrayList<>();
            for (Application app : applications) {
                Map<String, Object> applicationMap = new HashMap<>();
                applicationMap.put("applicationID", app.getId());
                applicationMap.put("positionTitle", positionService.getSelectedPosition(app.getPositionID()).getTitle());
                applicationMap.put("status", app.getStatus());
                applicationMap.put("applicationDate", app.getCreateDate().toString());
                applicationList.add(applicationMap);
            }
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("status", "success");
            responseMap.put("applications", applicationList);
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Map<String, Object>> getApplicationDetails(@PathVariable Integer id, @RequestParam String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            Optional<Application> application = applicationRepository.findByIdAndCandidateID(id, user.get().getId());
            if (application.isPresent()) {
                Map<String, Object> applicationMap = new HashMap<>();
                Application app = application.get();
                applicationMap.put("status", "SUCCESS");
                applicationMap.put("application", Map.of(
                        "applicationID", app.getId(),
                        "candidateName", user.get().getUsername(),
                        "positionTitle", positionService.getSelectedPosition(app.getPositionID()).getTitle(),
                        "status", app.getStatus(),
                        "cv", "https://example.com/cv/" + user.get().getUsername() + ".pdf",
                        "applicationDate", app.getCreateDate().toString())
                );
                return new ResponseEntity<>(applicationMap, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

