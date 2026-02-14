package com.hpy.ops360.synergyservice.response.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmHistoryNTicketsResponse implements Serializable {

	private String requestid;
	private String atmid;

	@JsonProperty("TicketDetails")
	private List<TicketDetailsDto> ticketDetails;

}
