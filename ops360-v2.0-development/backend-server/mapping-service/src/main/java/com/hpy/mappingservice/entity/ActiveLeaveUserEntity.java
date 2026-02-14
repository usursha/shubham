package com.hpy.mappingservice.entity;

import java.time.LocalDateTime;

import org.apache.james.mime4j.dom.datetime.DateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ActiveLeaveUserEntity {

	@Id
	@Column(name = "sr_no")
	private Integer srNo;

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

	@Column(name = "raw_starttime")
	private LocalDateTime rawStarttime;

	@Column(name = "raw_endtime")
	private LocalDateTime rawEndtime;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "city")
	private String city;

	@Column(name = "address")
	private String address;

	@Column(name = "mapped_atm")
	private Integer mappedAtm;

	@Column(name = "total_atm")
	private Integer totalAtm;
	
	@Column(name = "temp_ce")
	private Integer tempCe;

	@Column(name = "profile_pic")
	private String profilePic;
}