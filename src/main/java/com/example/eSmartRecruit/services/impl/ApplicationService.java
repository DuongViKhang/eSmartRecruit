package com.example.eSmartRecruit.services.impl;

import com.example.eSmartRecruit.models.Application;

import com.example.eSmartRecruit.repositories.ApplicationRepos;
import com.example.eSmartRecruit.services.IApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<Application> getApplicationsByCandidateId(Integer candidateID) {
        return applicationRepository.findByCandidateID(candidateID);
    }
    public Optional<Application> getApplicationByIdAndCandidateId(Integer ID, Integer candidateID) {
        return applicationRepository.findByIdAndCandidateID(ID, candidateID);
    }
}
