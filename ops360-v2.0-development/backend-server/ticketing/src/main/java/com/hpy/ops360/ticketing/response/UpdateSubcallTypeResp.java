package com.hpy.ops360.ticketing.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSubcallTypeResp {
	
	@JsonProperty("responseCode")
	private int responseCode;
	
	@JsonProperty("message")
	private String message;
	
	@JsonProperty("data")
	private SynergyResponse data;

}
