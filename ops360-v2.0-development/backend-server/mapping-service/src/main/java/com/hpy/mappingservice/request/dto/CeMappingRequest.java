package com.hpy.mappingservice.request.dto;

import lombok.Data;

@Data
public class CeMappingRequest {
	
	private String username;
	
	private String assignedCes;
	
	private String unAssignedCes;

}
