package com.example.eSmartRecruit.repositories;

import com.example.eSmartRecruit.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepos extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String esmail);

    boolean existsByUsername(String username);

}
