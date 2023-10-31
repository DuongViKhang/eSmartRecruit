package com.example.eSmartRecruit.services.impl;

import com.example.eSmartRecruit.models.Application;

import com.example.eSmartRecruit.repositories.ApplicationRepos;
import com.example.eSmartRecruit.services.IApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

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

    @Override
    public String update(Application applications, Integer id) {
        Application exApplication = applicationRepository.findById(id).orElse(null);
        exApplication.setCv(applications.getCv());
        exApplication.setUpdateDate(Date.valueOf(LocalDate.now()));
        return null;
    }


}
