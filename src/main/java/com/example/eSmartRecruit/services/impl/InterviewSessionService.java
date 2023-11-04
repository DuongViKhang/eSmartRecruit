package com.example.eSmartRecruit.services.impl;

import com.example.eSmartRecruit.models.InterviewSession;
import com.example.eSmartRecruit.repositories.InterviewSessionRepos;
import com.example.eSmartRecruit.services.IInterviewSessionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InterviewSessionService implements IInterviewSessionService {
    private final InterviewSessionRepos interviewSessionRepos;
    public List<InterviewSession> findByInterviewerID(Integer userId) {
        return  interviewSessionRepos.findByInterviewerID(userId);
    }
    public  InterviewSession findByID(Integer ID) {
        return  interviewSessionRepos.findById(ID).orElse(null);
    }
}
