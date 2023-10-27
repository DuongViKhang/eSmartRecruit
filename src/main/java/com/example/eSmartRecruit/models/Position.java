package com.example.eSmartRecruit.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "Positions")
//@AllArgsConstructor
//@NoArgsConstructor
@Data
@Builder
public class Position {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "Title", length = 255)
    private String title;

    @Lob
    @Column(name = "JobDescription", columnDefinition = "text")
    private String jobDescription;

    @Lob
    @Column(name = "JobRequirements", columnDefinition = "text")
    private String jobRequirements;

    @Column(name = "Salary")
    private BigDecimal salary;

    @Column(name = "PostDate")
    private Date postDate;

    @Column(name = "ExpireDate")
    private Date expireDate;

    @Column(name = "Location", length = 255)
    private String location;
    
    public Position() {}

	public Position(String title, String jobDescription, String jobRequirements, BigDecimal salary, Date postDate,
			Date expireDate, String location) {
		super();
		this.title = title;
		this.jobDescription = jobDescription;
		this.jobRequirements = jobRequirements;
		this.salary = salary;
		this.postDate = postDate;
		this.expireDate = expireDate;
		this.location = location;
	}

	public Position(Integer id, String title, String jobDescription, String jobRequirements, BigDecimal salary,
			Date postDate, Date expireDate, String location) {
		super();
		this.id = id;
		this.title = title;
		this.jobDescription = jobDescription;
		this.jobRequirements = jobRequirements;
		this.salary = salary;
		this.postDate = postDate;
		this.expireDate = expireDate;
		this.location = location;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getJobRequirements() {
		return jobRequirements;
	}

	public void setJobRequirements(String jobRequirements) {
		this.jobRequirements = jobRequirements;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
    
}
