package com.example.eSmartRecruit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eSmartRecruit.models.Skill;

@Repository
public interface SkillRepos extends JpaRepository<Skill, Long>{

}
