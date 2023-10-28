package com.example.eSmartRecruit.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.sql.Date;

@Entity
@Table(name = "Blacklists")
public class Blacklist {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "CandidateID")
    private Integer candidateID;

    @Lob
    @Column(name = "Reason", columnDefinition = "text")
    private String reason;

    @Temporal(TemporalType.DATE)
    @Column(name = "CreateDate")
    private Date createDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "UpdateDate")
    private Date updateDate;
    
    public Blacklist() {}

	public Blacklist(Integer candidateID, String reason, Date createDate, Date updateDate) {
		super();
		this.candidateID = candidateID;
		this.reason = reason;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public Blacklist(Integer id, Integer candidateID, String reason, Date createDate, Date updateDate) {
		super();
		this.id = id;
		this.candidateID = candidateID;
		this.reason = reason;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCandidateID() {
		return candidateID;
	}

	public void setCandidateID(Integer candidateID) {
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