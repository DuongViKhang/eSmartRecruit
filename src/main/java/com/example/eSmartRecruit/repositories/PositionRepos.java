package com.example.eSmartRecruit.repositories;

import com.example.eSmartRecruit.models.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepos extends JpaRepository<Position, Integer>{

}
