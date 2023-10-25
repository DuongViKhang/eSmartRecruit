package com.example.eSmartRecruit.service;

import com.example.eSmartRecruit.models.Applications;
import com.example.eSmartRecruit.repository.ApplicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApplicationService {
    ApplicationRepository applicationRepository;
    public String apply(Applications application){
        try{
            applicationRepository.save(application);
            return "Successfully applied";
        }catch (Exception e){
            return e.toString();
        }
    }
}
