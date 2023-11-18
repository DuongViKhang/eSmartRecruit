package com.example.eSmartRecruit.services.impl;

import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.exception.ApplicationException;
import com.example.eSmartRecruit.exception.NotFoundException;
import com.example.eSmartRecruit.exception.UnauthorizedAccessException;
import com.example.eSmartRecruit.models.Application;
import com.example.eSmartRecruit.models.enumModel.ApplicationStatus;
import com.example.eSmartRecruit.repositories.ApplicationRepos;
import com.example.eSmartRecruit.services.IApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ApplicationService implements IApplicationService {
    @Autowired
    private final ApplicationRepos applicationRepository;
    public String apply(Application application){
        try{
            if(isApplied(application.getCandidateID(),application.getPositionID())){
                throw new ApplicationException(ResponseObject.APPLICATION_EXISTED);
            }

            applicationRepository.save(application);
            return ResponseObject.APPLY_SUCCESS;
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
    public Application getApplicationById(Integer ID) throws ApplicationException {
        return applicationRepository.findById(ID).orElseThrow(()->new ApplicationException(ResponseObject.APPLICATION_NOT_FOUND));
    }


    public String update(Integer candidateId, Application applications, Integer id) {
        try{
            Application exApplication = applicationRepository.findById(id).orElseThrow(()->new ApplicationException(ResponseObject.APPLICATION_NOT_FOUND));
            if(!candidateId.equals(exApplication.getCandidateID())){
                throw new ApplicationException(ResponseObject.NOT_YOUR_APPLICATION);
            }

            exApplication.setCv(applications.getCv());
            exApplication.setUpdateDate(Date.valueOf(LocalDate.now()));
            applicationRepository.save(exApplication);
            return ResponseObject.UPDATED_SUCCESS;
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
                return ResponseObject.APPROVE;
            }
            else if (status == ApplicationStatus.Declined){
                return ResponseObject.DECLINE;
            }
        }catch (Exception e){
            return e.toString();
        }
        return null;
    }

    public Boolean isPresent(Integer jobid){
        try{
            Application application = applicationRepository.findById(jobid).orElseThrow(()->new ApplicationException(ResponseObject.APPLICATION_NOT_FOUND));
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
            Application application = applicationRepository.findById(jobid).orElseThrow(() -> new NotFoundException("Cant find this application!"));
            if(!isPresent(jobid)){
                throw new NotFoundException("Cant find this application!");
            }
            if(!candidateId.equals(application.getCandidateID())){
                throw new UnauthorizedAccessException("This is not your application!");
            }
            applicationRepository.deleteById(jobid);
            return ResponseObject.DELETED_SUCCESS;
        }catch (Exception e){
            return e.getMessage();
        }
    }

    public Long getcountApplication() {
        return applicationRepository.count();
    }

    public Application findById(int id) throws NotFoundException {
        return applicationRepository.findById(id).orElseThrow(() -> new NotFoundException("Application not found!"));
    }
}