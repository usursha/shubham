package com.hpy.ops360.ticketing.feignclient.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallRequestDto {

	
	
	private String agentNumber; // field engineer contact
	
	
	private String customerNumber; // our number

}
