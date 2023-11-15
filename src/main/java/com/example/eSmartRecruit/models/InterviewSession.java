package com.example.eSmartRecruit.models;

import com.example.eSmartRecruit.models.enumModel.SessionResult;
import com.example.eSmartRecruit.models.enumModel.SessionStatus;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;


import java.sql.Date;

@Entity
@Table(name = "InterviewSessions")
@Data
@AllArgsConstructor
public class InterviewSession {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;


//    @Column(name = "PositionID")
//    private Integer positionID;

    @Column(name = "InterviewerID")
    private Integer interviewerID;

//    @Column(name = "CandidateID")
//    private Integer candidateID;
	@Column(name = "ApplicationID")
	private Integer ApplicationID;


    @Column(name = "Date")
    private Date date;

    @Column(name = "Location", length = 255)
    private String location;

    @Column(name = "Status")
	@Enumerated(EnumType.STRING)
    private SessionStatus status;

    @Column(name = "Result")
	@Enumerated(EnumType.STRING)
    private SessionResult result;

    @Lob
    @Column(name = "Notes", columnDefinition = "text")
    private String notes;


    public InterviewSession() {}

}
