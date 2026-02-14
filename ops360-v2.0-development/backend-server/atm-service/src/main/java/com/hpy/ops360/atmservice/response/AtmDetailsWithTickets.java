package com.hpy.ops360.atmservice.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AtmDetailsWithTickets {

	@JsonProperty("atmid")
	private String atmId;

	@JsonProperty("openticketdetails")
	private List<TicketDetailsDto> openticketdetails;
}
