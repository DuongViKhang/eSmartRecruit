package com.example.eSmartRecruit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eSmartRecruit.models.InterviewSession;

@Repository
public interface InterviewSessionRepos extends JpaRepository<InterviewSession, Long>{

}
