package com.hpy.ops360.atmservice.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hpy.ops360.atmservice.response.TicketDetailsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmHistoryNTicketsResponse implements Serializable {


	private static final long serialVersionUID = 1L;
	private String requestid;
	private String atmid;

	@JsonProperty("TicketDetails")
	private List<TicketDetailsDto> ticketDetails;

}
