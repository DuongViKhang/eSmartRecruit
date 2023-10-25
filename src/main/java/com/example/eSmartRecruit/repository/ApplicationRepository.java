package com.example.eSmartRecruit.repository;

import com.example.eSmartRecruit.models.Applications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Applications,Integer> {
}
