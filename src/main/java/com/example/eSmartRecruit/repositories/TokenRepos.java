package com.example.eSmartRecruit.repositories;

import com.example.eSmartRecruit.models.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TokenRepos extends JpaRepository<TokenEntity, Integer> {
    Optional<TokenEntity> findByUsername(String username);

    @Transactional
    @Modifying
    @Query("DELETE FROM TokenEntity t WHERE t.username = ?1")
    void deleteByUsername(String username);
}
