package com.example.eSmartRecruit.repositories;

import com.example.eSmartRecruit.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepos extends JpaRepository<Report, Integer>{

    Optional<Report> findBySessionID(Integer id);
}
