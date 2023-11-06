package com.example.eSmartRecruit.services.impl;

import com.example.eSmartRecruit.models.Report;
import com.example.eSmartRecruit.repositories.ReportRepos;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReportService {
    private  ReportRepos reportRepos;
    public String reportInterviewSession(Report report) {
        try{
            reportRepos.save(report);
            return "Report Successfully!";
        }catch (Exception e){
            return e.getMessage();
        }
    }
}