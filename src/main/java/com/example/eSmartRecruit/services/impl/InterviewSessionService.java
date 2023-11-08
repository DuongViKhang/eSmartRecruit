package com.example.eSmartRecruit.services.impl;


import com.example.eSmartRecruit.repositories.InterviewSessionRepos;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class InterviewSessionService {
    private InterviewSessionRepos interviewSessionRepos;
    public long getCountInterview(){
        return interviewSessionRepos.findAll().size();
    }
}
