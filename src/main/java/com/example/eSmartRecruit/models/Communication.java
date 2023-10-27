package com.example.eSmartRecruit.models;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "Communications")
public class Communication {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "CandidateID")
    private int candidateID;

    @Lob
    @Column(name = "Notes")
    private String notes;

    @Column(name = "DateContacted")
    private Date dateContacted;

    @Temporal(TemporalType.DATE)
    @Column(name = "CreateDate")
    private Date createDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "UpdateDate")
    private Date updateDate;

    public Communication() {}

	public Communication(int candidateID, String notes, Date dateContacted, Date createDate, Date updateDate) {
		super();
		this.candidateID = candidateID;
		this.notes = notes;
		this.dateContacted = dateContacted;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public Communication(int id, int candidateID, String notes, Date dateContacted, Date createDate, Date updateDate) {
		super();
		this.id = id;
		this.candidateID = candidateID;
		this.notes = notes;
		this.dateContacted = dateContacted;
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getDateContacted() {
		return dateContacted;
	}

	public void setDateContacted(Date dateContacted) {
		this.dateContacted = dateContacted;
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
