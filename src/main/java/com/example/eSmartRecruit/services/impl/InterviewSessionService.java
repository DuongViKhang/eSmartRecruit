package com.example.eSmartRecruit.services.impl;

import com.example.eSmartRecruit.controllers.request_reponse.request.InterviewSessionRequest;
import com.example.eSmartRecruit.exception.InterviewSessionException;
import com.example.eSmartRecruit.exception.PositionException;
import com.example.eSmartRecruit.models.InterviewSession;
import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.models.enumModel.SessionStatus;
import com.example.eSmartRecruit.repositories.InterviewSessionRepos;
import com.example.eSmartRecruit.services.IInterviewSessionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InterviewSessionService implements IInterviewSessionService {
    private final InterviewSessionRepos interviewSessionRepos;
    public List<InterviewSession> findByInterviewerID(Integer userId) throws InterviewSessionException {
        return  interviewSessionRepos.findByInterviewerID(userId);
    }
    public  InterviewSession findByID(Integer ID) throws InterviewSessionException {
        return  interviewSessionRepos.findById(ID).orElseThrow(()->new InterviewSessionException("The required interview session not found"));
    }

    public boolean isAlready(Integer interviewersessionID) throws InterviewSessionException {
        InterviewSession interviewSession = interviewSessionRepos.findById(interviewersessionID).orElseThrow(()->new InterviewSessionException("The required position not found"));
        if(interviewSession.getStatus() != SessionStatus.Already ){
            return false;
        }
        return true;
    }

    public Long getCountInterview() {
        return interviewSessionRepos.count();
    }
    public void save(InterviewSession interviewSession){
        interviewSessionRepos.save(interviewSession);
    }
    public InterviewSession scheduleInterview(int id, InterviewSessionRequest interviewSessionRequest) throws InterviewSessionException {
        InterviewSession interviewSession = findByID(id);

        interviewSession.setInterviewerID(interviewSessionRequest.getInterviewerId());
        interviewSession.setDate(interviewSessionRequest.getDate());
        interviewSession.setLocation(interviewSessionRequest.getLocation());
        interviewSession.setStatus(SessionStatus.Yet);
        interviewSession.setNotes(interviewSessionRequest.getNotes());

        save(interviewSession);
        return interviewSession;
    }
}
