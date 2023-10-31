package com.example.eSmartRecruit.models;

import com.example.eSmartRecruit.models.enumModel.Role;
import com.example.eSmartRecruit.models.enumModel.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "Users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User implements UserDetails {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "Username", unique = true)
    private String username;

    @Column(name = "Password")
    private String password;

    @Column(name = "Email", unique = true)
    private String email;

    @Column(name = "PhoneNumber", unique = true)
    private String phoneNumber;
    
    @Column(name = "RoleName")
	@Enumerated(EnumType.STRING)
    private Role roleName;

    @Column(name = "Status")
	@Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "CreateDate")
    private Date createDate;

    @Column(name = "UpdateDate")
    private Date updateDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	public String getPhoneNumber() {
//		return phoneNumber;
//	}
//
//	public void setPhoneNumber(String phoneNumber) {
//		this.phoneNumber = phoneNumber;
//	}
//
//	public String getRoleName() {
//		return roleName;
//	}
//
//	public void setRoleName(String roleName) {
//		this.roleName = roleName;
//	}
//
//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}
//
//	public Date getCreateDate() {
//		return createDate;
//	}
//
//	public void setCreateDate(Date createDate) {
//		this.createDate = createDate;
//	}
//
//	public Date getUpdateDate() {
//		return updateDate;
//	}
//
//	public void setUpdateDate(Date updateDate) {
//		this.updateDate = updateDate;
//	}
    
//    public User() {}
//
	public User(String username, String password, String email, String phoneNumber, String roleName, String status,
			Date createDate, Date updateDate) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.roleName = Role.valueOf(roleName);
		this.status = UserStatus.valueOf(status);
		this.createDate = createDate;
		this.updateDate = updateDate;
	}
//
//	public User(Integer id, String username, String password, String email, String phoneNumber, String roleName,
//			String status, Date createDate, Date updateDate) {
//		super();
//		this.id = id;
//		this.username = username;
//		this.password = password;
//		this.email = email;
//		this.phoneNumber = phoneNumber;
//		this.roleName = roleName;
//		this.status = status;
//		this.createDate = createDate;
//		this.updateDate = updateDate;
//	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(this.getRoleName().name()));
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return UserStatus.Active.equals(this.status);
	}



}
