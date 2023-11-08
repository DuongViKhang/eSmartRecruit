package com.example.eSmartRecruit.services.impl;

import com.example.eSmartRecruit.exception.ApplicationException;
import com.example.eSmartRecruit.models.Application;

import com.example.eSmartRecruit.models.enumModel.ApplicationStatus;
import com.example.eSmartRecruit.repositories.ApplicationRepos;
import com.example.eSmartRecruit.services.IApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String adminUpdate(Integer id, ApplicationStatus status){
        try{
            Application exApplication = applicationRepository.findById(id).orElseThrow(()->new ApplicationException("Application not found!"));
            exApplication.setStatus(status);
            exApplication.setUpdateDate(Date.valueOf(LocalDate.now()));
            applicationRepository.save(exApplication);
            if(status == ApplicationStatus.Approved){
                return "Approve application successfully!";
            }
            else if (status == ApplicationStatus.Declined){
                return "Decline application successfully!";
            }
        }catch (Exception e){
            return e.toString();
        }
        return null;
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
    public Optional<Application> findById(int id){
        return applicationRepository.findById(id);
    }
}
