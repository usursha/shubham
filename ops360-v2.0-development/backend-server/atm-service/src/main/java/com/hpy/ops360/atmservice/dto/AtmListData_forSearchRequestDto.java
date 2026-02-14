package com.hpy.ops360.atmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AtmListData_forSearchRequestDto {

	private String userId;
	private String bank;
	private String grade;
	private String status;
	private String uptime_status;
	
	
}
