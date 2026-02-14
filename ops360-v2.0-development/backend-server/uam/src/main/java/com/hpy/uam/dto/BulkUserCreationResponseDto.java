package com.hpy.uam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkUserCreationResponseDto {
	

	private String username;
	
	private String firstName;
	
	private String lastName;

	private String email;

	private String groupName;

	private String mobile;
	
	private String status;
	
}
