package com.example.eSmartRecruit.services.impl;

import com.example.eSmartRecruit.exception.ApplicationException;
import com.example.eSmartRecruit.models.Application;

import com.example.eSmartRecruit.repositories.ApplicationRepos;
import com.example.eSmartRecruit.services.IApplicationService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import java.sql.Date;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class ApplicationService implements IApplicationService {
    @Autowired
    private final ApplicationRepos applicationRepository;
    public String apply(Application application){
        try{
            if(isApplied(application.getCandidateID(),application.getPositionID())){
                throw new ApplicationException("You have already applied to this position!");
            }

            applicationRepository.save(application);
            return "Successfully applied";
        }catch (Exception e){
            return e.getMessage();
        }
    }

    public boolean isApplied(Integer candidateID, Integer positionID){
        Application application = applicationRepository.findByCandidateIDAndPositionID(candidateID, positionID).orElse(null);
        if(application==null){
            return false;
        }
        return true;
    }

    public List<Application> getApplicationsByCandidateId(Integer candidateID) {
        return applicationRepository.findByCandidateID(candidateID);
    }
    public Application getApplicationById(Integer ID, Integer candidateID) throws ApplicationException {
        return applicationRepository.findById(ID).orElseThrow(()->new ApplicationException("Cant find the required application!"));
    }
//    public List<Application> getAllApplication() {
//        return  applicationRepository.findAll();
//    }
    public Optional<Application> getApplicationById(Integer id){
        return applicationRepository.findById(id);
    }

    public long getcountApplication(){
        return applicationRepository.count();
    }


    public String update(Integer candidateId, Application applications, Integer id) {
        try{
            Application exApplication = applicationRepository.findById(id).orElseThrow(()->new ApplicationException("Application not found!"));
            if(!candidateId.equals(exApplication.getCandidateID())){
                throw new ApplicationException("This is not your application!");
            }

            exApplication.setCv(applications.getCv());
            exApplication.setUpdateDate(Date.valueOf(LocalDate.now()));
            applicationRepository.save(exApplication);
            return "update Success";
        }catch (Exception e){
            return e.toString();
        }

    }

    public Boolean isPresent(Integer jobid){
        try{
            Application application = applicationRepository.findById(jobid).orElseThrow(()->new ApplicationException("Cant find this application!"));
            if(application == null){
                return false;
            }
            return true;

        }catch (Exception e){
            return false;
        }
    }
    public String deletejob(Integer candidateId, Integer jobid){
        try{
            Application application = applicationRepository.findById(jobid).orElseThrow(()->new ApplicationException("Cant find this application!"));
            if(!isPresent(jobid)){
                throw new ApplicationException("Cant find this application!");
            }
            if(!candidateId.equals(application.getCandidateID())){
                throw new ApplicationException("This is not your application!");
            }
            applicationRepository.deleteById(jobid);
            return "Successfully deleted!";
        }catch (Exception e){
            return e.getMessage();
        }
    }
}
