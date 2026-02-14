package com.hpy.ops360.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Call2CallRequestDto {
	
	@JsonProperty("k_number")
	private String kNumber; //virtual number
	
	@JsonProperty("agent_number")
	private String agentNumber; // field engineer contact
	
	@JsonProperty("customer_number")
	private String customerNumber; // our number
	
	@JsonProperty("caller_id")
	private String callerId; // which will be displayed on call

}
