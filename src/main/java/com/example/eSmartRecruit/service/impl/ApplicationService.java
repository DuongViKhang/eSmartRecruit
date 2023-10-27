package com.example.eSmartRecruit.service.impl;

import com.example.eSmartRecruit.models.Application;

import com.example.eSmartRecruit.repositories.ApplicationRepos;
import com.example.eSmartRecruit.service.IApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApplicationService implements IApplicationService {
    ApplicationRepos applicationRepository;
    public String apply(Application application){
        try{
            applicationRepository.save(application);
            return "Successfully applied";
        }catch (Exception e){
            return e.toString();
        }
    }
}
