package com.example.eSmartRecruit.service.impl;

import com.example.eSmartRecruit.models.Applications;
import com.example.eSmartRecruit.repository.ApplicationRepository;
import com.example.eSmartRecruit.service.IApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApplicationService implements IApplicationService {
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
