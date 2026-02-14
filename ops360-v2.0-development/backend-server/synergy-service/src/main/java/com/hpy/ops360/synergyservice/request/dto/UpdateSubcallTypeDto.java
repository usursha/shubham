package com.hpy.ops360.synergyservice.request.dto;

import lombok.Data;

@Data
public class UpdateSubcallTypeDto {
	
	private String ticketNo;
	private String atmId;
	private String subcallType;
	private String comments;
	private String updatedBy;

}
