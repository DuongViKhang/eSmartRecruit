package com.example.eSmartRecruit.services.impl;

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
    public List<InterviewSession> findByInterviewerID(Integer userId) {
        return  interviewSessionRepos.findByInterviewerID(userId);
    }
    public  InterviewSession findByID(Integer ID) throws InterviewSessionException {
//        try {
//            Optional<InterviewSession> interviewSession = interviewSessionRepos.findById(ID);
//            if (interviewSession.isPresent()) {
//                return  interviewSession.orElseThrow(()->new InterviewSessionException("The required interview Session not found"))
//            } else return null;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
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
}
