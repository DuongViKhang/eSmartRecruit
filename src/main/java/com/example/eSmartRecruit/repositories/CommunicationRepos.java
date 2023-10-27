package com.example.eSmartRecruit.repositories;

import com.example.eSmartRecruit.models.Communication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunicationRepos extends JpaRepository<Communication, Integer>{

}
