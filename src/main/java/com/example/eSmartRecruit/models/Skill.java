package com.example.eSmartRecruit.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Skills")
public class Skill {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
    private int id;

    @Column(name = "CandidateID")
    private int candidateId;

    @Column(name = "SkillName")
    private String skillName;
    
    public Skill() {}

    public Skill(int candidateId, String skillName) {
		super();
		this.candidateId = candidateId;
		this.skillName = skillName;
	}
    
	public Skill(int id, int candidateId, String skillName) {
		super();
		this.id = id;
		this.candidateId = candidateId;
		this.skillName = skillName;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidate(int candidateId) {
        this.candidateId = candidateId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
}