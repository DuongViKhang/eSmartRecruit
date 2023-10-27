package com.example.eSmartRecruit.models;

import java.sql.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "Applications")
public class Application {
	
	public static enum Status {
	    Pending,
	    Approved,
	    Declined
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "CandidateID")
    private int candidateID;

    @Column(name = "PositionID")
    private int positionID;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private Status status;

    @Column(name = "CV", length = 255)
    private String cv;

    @Temporal(TemporalType.DATE)
    @Column(name = "CreateDate")
    private Date createDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "UpdateDate")
    private Date updateDate;

    public Application() {}

	public Application(int candidateID, int positionID, Status status, String cv, Date createDate, Date updateDate) {
		super();
		this.candidateID = candidateID;
		this.positionID = positionID;
		this.status = status;
		this.cv = cv;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public Application(int id, int candidateID, int positionID, Status status, String cv, Date createDate,
			Date updateDate) {
		super();
		this.id = id;
		this.candidateID = candidateID;
		this.positionID = positionID;
		this.status = status;
		this.cv = cv;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCandidateID() {
		return candidateID;
	}

	public void setCandidateID(int candidateID) {
		this.candidateID = candidateID;
	}

	public int getPositionID() {
		return positionID;
	}

	public void setPositionID(int positionID) {
		this.positionID = positionID;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getCv() {
		return cv;
	}

	public void setCv(String cv) {
		this.cv = cv;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}
