package com.example.eSmartRecruit.models;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {
	
	public static enum Role {
        Candidate,
        Admin,
        Interviewer
    }

    public static enum Status {
        Active,
        Inactive
    }
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "Username", unique = true)
    private String username;

    @Column(name = "Password")
    private String password;

    @Column(name = "Email", unique = true)
    private String email;

    @Column(name = "PhoneNumber", unique = true)
    private String phoneNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "RoleName")
    private Role roleName;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private Status status;

    @Column(name = "CreateDate")
    private Date createDate;

    @Column(name = "UpdateDate")
    private Date updateDate;
    
    public User() {}

	public User(String username, String password, String email, String phoneNumber, Role roleName, Status status,
			Date createDate, Date updateDate) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.roleName = roleName;
		this.status = status;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public User(int id, String username, String password, String email, String phoneNumber, Role roleName,
			Status status, Date createDate, Date updateDate) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.roleName = roleName;
		this.status = status;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Role getRoleName() {
		return roleName;
	}

	public void setRoleName(Role roleName) {
		this.roleName = roleName;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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
