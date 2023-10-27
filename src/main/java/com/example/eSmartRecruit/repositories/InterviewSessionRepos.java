package com.example.eSmartRecruit.repositories;

import com.example.eSmartRecruit.models.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewSessionRepos extends JpaRepository<InterviewSession, Integer>{

}
