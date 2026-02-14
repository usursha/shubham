package com.hpy.ops360.ticketing.cm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketHistoryArchiveRequestDto {

	@JsonProperty(value = "startdate")
	private String startDate;

	@JsonProperty(value = "enddate")
	private String endDate;

	@JsonProperty(value = "bankname")
	private String bankName;

	@JsonProperty(value = "atmid")
	private String atmId;

	@JsonProperty(value = "ticketno")
	private String ticketNo;
	
	@JsonProperty(value = "userid")
	private String userId;

}
