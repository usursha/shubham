package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="user_mast")
public class UserMast {
	
	@Id
	@Column(name="iUser")
	private int iUser;
	
	@Column(name="username")
	private String username;
	
	@Column(name="iRole")
	private int iRole;
	
	@Column(name="mobileno")
	private String mobileNo;
	
	@Column(name="display_name")
	private String displayName;

	@Column(name="acitve_user")
	private int acitveUser;
	
	@Column(name="postal_address")
	private String postalAddress;
	
	@Column(name="employee_code")
	private String employeeCode;
	
	@Column(name="circle_area")
	private String circleArea;
	
	@Column(name="user_email")
	private String userEmail;
	
	@Column(name="zone")
	private String zone;
}
