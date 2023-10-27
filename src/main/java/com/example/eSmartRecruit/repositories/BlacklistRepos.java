package com.example.eSmartRecruit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eSmartRecruit.models.Blacklist;

@Repository
public interface BlacklistRepos extends JpaRepository<Blacklist, Long>{

}
