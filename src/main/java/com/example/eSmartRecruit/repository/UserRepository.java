package com.example.eSmartRecruit.repository;

import com.example.eSmartRecruit.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Integer> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByUserName(String userName);
}
