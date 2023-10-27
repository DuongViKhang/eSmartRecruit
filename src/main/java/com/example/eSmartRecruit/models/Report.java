package com.example.eSmartRecruit.models;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "Reports")
public class Report {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "SessionID")
    private int sessionID;

    @Column(name = "ReportName", length = 255)
    private String reportName;

    @Lob
    @Column(name = "ReportData")
    private String reportData;

    @Temporal(TemporalType.DATE)
    @Column(name = "CreateDate")
    private Date createDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "UpdateDate")
    private Date updateDate;

    public Report() {}

	public Report(int sessionID, String reportName, String reportData, Date createDate, Date updateDate) {
		super();
		this.sessionID = sessionID;
		this.reportName = reportName;
		this.reportData = reportData;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public Report(int id, int sessionID, String reportName, String reportData, Date createDate, Date updateDate) {
		super();
		this.id = id;
		this.sessionID = sessionID;
		this.reportName = reportName;
		this.reportData = reportData;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSessionID() {
		return sessionID;
	}

	public void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
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