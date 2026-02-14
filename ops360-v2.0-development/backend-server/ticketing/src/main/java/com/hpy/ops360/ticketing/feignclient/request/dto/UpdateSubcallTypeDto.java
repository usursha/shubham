package com.hpy.ops360.ticketing.feignclient.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateSubcallTypeDto {
	
	private String ticketNo;
	private String atmId;
	private String subcallType;
	private String comments;
	private String updatedBy;

}
