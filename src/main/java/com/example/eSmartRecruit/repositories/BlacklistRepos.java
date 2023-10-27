package com.example.eSmartRecruit.repositories;

import com.example.eSmartRecruit.models.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistRepos extends JpaRepository<Blacklist, Integer>{

}
