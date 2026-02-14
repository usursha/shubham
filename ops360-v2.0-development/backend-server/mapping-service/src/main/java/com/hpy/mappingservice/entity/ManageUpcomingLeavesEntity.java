package com.hpy.mappingservice.entity;

import java.time.LocalDateTime;

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
public class ManageUpcomingLeavesEntity {
	@Id
	private Long sr_no;
	
	@Column(name = "user_id")
	private Long user_id;
	
	@Column(name = "user_name")
	private String user_name;
	
	@Column(name = "leave_id")
	private String leave_id;
	
	@Column(name = "start_range")
	private String start_range;
	
	@Column(name = "end_range")
	private String end_range;
	
	@Column(name = "raw_starttime")
	private LocalDateTime raw_startTime;
	
	@Column(name = "raw_endtime")
	private LocalDateTime raw_endTime;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "full_name")
	private String full_name;
	
	@Column(name = "employee_id")
	private String employee_id;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "mapped_atm")
	private int mapped_atm;
	
	@Column(name = "temp_ce")
	private String temporary_ce;
	
	@Column(name = "total_atms")
	private int total_atm;
	
	@Column(name = "percentage_mapped")
	private String percentage;
	
	@Column(name = "Flag")
	private int flag;
	
	@Column(name = "atm_code")
	private String atm_code;
	
	@Column(name = "profile_pic")
	private String profilePic;

}
