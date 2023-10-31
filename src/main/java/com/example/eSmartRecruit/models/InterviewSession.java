package com.example.eSmartRecruit.models;

import com.example.eSmartRecruit.models.enumModel.SessionResult;
import com.example.eSmartRecruit.models.enumModel.SessionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "InterviewSessions")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InterviewSession {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "InterviewerID")
    private Integer interviewerID;

    @Column(name = "ApplicationID")
    private Integer applicationID;

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
}
