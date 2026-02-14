package com.hpy.ops360.synergyservice.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemarksrequestDto {

	@JsonProperty("ticketno")
	private String ticketNo;

	@JsonProperty("atmid")
	private String atmId;

}
