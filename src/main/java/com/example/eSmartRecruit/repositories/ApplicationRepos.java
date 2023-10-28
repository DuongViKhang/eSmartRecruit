package com.example.eSmartRecruit.repositories;

import com.example.eSmartRecruit.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepos extends JpaRepository<Application, Integer>{

}
