package com.hpy.monitoringservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CEUsersDetailsDto {
	
	private String designation;

	private int loggedIn;

	private int total;


}
