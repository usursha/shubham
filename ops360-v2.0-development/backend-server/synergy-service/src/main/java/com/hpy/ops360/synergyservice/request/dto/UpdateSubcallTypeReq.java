package com.hpy.ops360.synergyservice.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Builder
public class UpdateSubcallTypeReq {
	
	@JsonProperty("requestid")
	private String requestid;
	
	
	@JsonProperty("ticketno")
	private String ticketNo;

	@JsonProperty("atmid")
	private String atmId;
	
	@JsonProperty("subcalltype")
	private String subcallType;
	
	@JsonProperty("comments")
	private String comments;
	
	@JsonProperty("updatedby")
	private String updatedBy;

}
