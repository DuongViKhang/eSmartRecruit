package com.example.eSmartRecruit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eSmartRecruit.models.User;

@Repository
public interface UserRepos extends JpaRepository<User, Long>{

}
