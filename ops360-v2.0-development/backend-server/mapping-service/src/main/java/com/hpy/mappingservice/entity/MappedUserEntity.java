package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MappedUserEntity {

	@Id
	@Column(name = "sr_no")
	private Long srno;

	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "employee_code")
	private String employeeCode;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "full_name")
	private String fullName;
	
	@Column(name = "address")
	private String address;

	@Column(name = "city")
	private String city;
	
	@Column(name = "mapped_atm")
	private Long mappedAtm;

	@Column(name = "assigned_atms")
	private Long assignedAtms;
	
	@Column(name = "remaining_atms")
	private Long remainingAtms;
	
	@Column(name = "profile_pic")
	private String profilepic;
}
