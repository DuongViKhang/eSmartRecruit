package com.example.eSmartRecruit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eSmartRecruit.models.Communication;

@Repository
public interface CommunicationRepos extends JpaRepository<Communication, Long>{

}
