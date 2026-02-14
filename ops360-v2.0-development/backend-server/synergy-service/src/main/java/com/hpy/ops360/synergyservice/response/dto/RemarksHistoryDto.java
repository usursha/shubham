package com.hpy.ops360.synergyservice.response.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemarksHistoryDto {

	@JsonProperty("requestid")
	private String requestid;

	@JsonProperty("ticketno")
	private String ticketno;

	@JsonProperty("atmid")
	private String atmid;

}
