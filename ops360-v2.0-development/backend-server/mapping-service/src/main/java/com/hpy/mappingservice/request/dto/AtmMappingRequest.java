package com.hpy.mappingservice.request.dto;

import lombok.Data;

@Data
public class AtmMappingRequest {
	
	private String username;
	
	private String assignedAtms;
	
	private String unAssignedAtms;

}
