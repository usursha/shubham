package com.hpy.mappingservice.response.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManageUpcomingLeavesCeResDto extends GenericDto {
	
	@JsonIgnore
	@Id
	private Long sr_no;
		
	private Long user_id;

	private String user_name;
	
	private String leave_id;
	
	private String start_range;
	
	private String end_range;
	
	private LocalDateTime raw_startTime;

	private LocalDateTime raw_endTime;
	
	private String city;
	
	private String full_name;
	
	private String employee_id;
	
	private String address;
	
	private int mapped_atm;
	
	private int total_atm;
	
	private String percentage;
	
	private int flag;
	
	private String profilePic;
}
