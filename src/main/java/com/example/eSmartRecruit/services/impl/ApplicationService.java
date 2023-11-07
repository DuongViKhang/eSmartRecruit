package com.example.eSmartRecruit.services.impl;

import com.example.eSmartRecruit.exception.ApplicationException;
import com.example.eSmartRecruit.models.Application;

import com.example.eSmartRecruit.repositories.ApplicationRepos;
import com.example.eSmartRecruit.services.IApplicationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ApplicationService implements IApplicationService {
    ApplicationRepos applicationRepository;
    public String apply(@Valid Application application){
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
    public Application getApplicationByIdAndCandidateId(Integer ID, Integer candidateID) throws ApplicationException {
        return applicationRepository.findByIdAndCandidateID(ID, candidateID).orElseThrow(()->new ApplicationException("Cant find the required application!"));
    }
}
