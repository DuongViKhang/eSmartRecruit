package com.example.eSmartRecruit.models;

import java.sql.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "InterviewSessions")
public class InterviewSession {
	
	public enum Status {
	    NotOnSchedule,
	    Yet,
	    Already
	}

	public enum Result {
	    NotYet,
	    Good,
	    NotGood
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "PositionID")
    private int positionID;

    @Column(name = "InterviewerID")
    private Integer interviewerID;

    @Column(name = "CandidateID")
    private int candidateID;

    @Column(name = "Date")
    private Date date;

    @Column(name = "Location", length = 255)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "Result")
    private Result result;

    @Lob
    @Column(name = "Notes")
    private String notes;

    public InterviewSession() {}

	public InterviewSession(int positionID, Integer interviewerID, int candidateID, Date date, String location,
			Status status, Result result, String notes) {
		super();
		this.positionID = positionID;
		this.interviewerID = interviewerID;
		this.candidateID = candidateID;
		this.date = date;
		this.location = location;
		this.status = status;
		this.result = result;
		this.notes = notes;
	}

	public InterviewSession(int id, int positionID, Integer interviewerID, int candidateID, Date date, String location,
			Status status, Result result, String notes) {
		super();
		this.id = id;
		this.positionID = positionID;
		this.interviewerID = interviewerID;
		this.candidateID = candidateID;
		this.date = date;
		this.location = location;
		this.status = status;
		this.result = result;
		this.notes = notes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPositionID() {
		return positionID;
	}

	public void setPositionID(int positionID) {
		this.positionID = positionID;
	}

	public Integer getInterviewerID() {
		return interviewerID;
	}

	public void setInterviewerID(Integer interviewerID) {
		this.interviewerID = interviewerID;
	}

	public int getCandidateID() {
		return candidateID;
	}

	public void setCandidateID(int candidateID) {
		this.candidateID = candidateID;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
    
}
