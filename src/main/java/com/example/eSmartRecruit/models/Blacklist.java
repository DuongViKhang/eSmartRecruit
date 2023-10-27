package com.example.eSmartRecruit.models;

import java.sql.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "Blacklists")
public class Blacklist {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "CandidateID")
    private int candidateID;

    @Lob
    @Column(name = "Reason")
    private String reason;

    @Temporal(TemporalType.DATE)
    @Column(name = "CreateDate")
    private Date createDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "UpdateDate")
    private Date updateDate;
    
    public Blacklist() {}

	public Blacklist(int candidateID, String reason, Date createDate, Date updateDate) {
		super();
		this.candidateID = candidateID;
		this.reason = reason;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public Blacklist(int id, int candidateID, String reason, Date createDate, Date updateDate) {
		super();
		this.id = id;
		this.candidateID = candidateID;
		this.reason = reason;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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