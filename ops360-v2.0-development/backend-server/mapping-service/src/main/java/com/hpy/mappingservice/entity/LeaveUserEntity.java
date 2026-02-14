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
public class LeaveUserEntity {

	@Id
	@Column(name = "sr_no")
	private Long srno;

	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "employee_code")
	private String employeeCode;

	@Column(name = "user_name")
	private String userName;
	
	@Column(name = "leave_id")
	private Long leaveId;

	@Column(name = "start_range")
	private String startRange;

	@Column(name = "end_range")
	private String endRange;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "email_id")
	private String emailId;
	
	@Column(name = "address")
	private String address;

	@Column(name = "mapped_atm")
	private Long mappedAtm;

	@Column(name = "total_atms")
	private Long totalAtms;
	
	@Column(name = "percentage_mapped")
	private String percentageMapped;
	
	@Column(name = "profile_pic")
	private String profilePic;
}
