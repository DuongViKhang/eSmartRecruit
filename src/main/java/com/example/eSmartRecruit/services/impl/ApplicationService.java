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


    public String update(Application applications, Integer id) {
        try{
            Application exApplication = applicationRepository.findById(id).orElse(null);
            exApplication.setCv(applications.getCv());
            exApplication.setUpdateDate(Date.valueOf(LocalDate.now()));
            applicationRepository.save(exApplication);
            return "update Success";
        }catch (Exception e){
            return e.getMessage();
        }

    }

    public Boolean isPresent(Integer jobid){
        try{
            Application application = applicationRepository.findById(jobid).orElse(null);
            if(application == null){
                return false;
            }
            return true;

        }catch (Exception e){
            return false;
        }
    }
    public Boolean deletejob(Integer jobid){
        try{
            if(!isPresent(jobid)){
                return false;
            }
            applicationRepository.deleteById(jobid);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
