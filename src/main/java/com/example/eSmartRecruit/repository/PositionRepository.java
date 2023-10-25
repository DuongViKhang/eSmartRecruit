package com.example.eSmartRecruit.repository;

import com.example.eSmartRecruit.models.Positions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Positions,Integer> {

}
