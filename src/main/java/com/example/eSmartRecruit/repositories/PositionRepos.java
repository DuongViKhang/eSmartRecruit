package com.example.eSmartRecruit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eSmartRecruit.models.Position;

@Repository
public interface PositionRepos extends JpaRepository<Position, Long>{

}
