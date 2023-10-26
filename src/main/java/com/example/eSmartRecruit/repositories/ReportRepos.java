package com.example.eSmartRecruit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eSmartRecruit.models.Report;

@Repository
public interface ReportRepos extends JpaRepository<Report, Long>{

}
