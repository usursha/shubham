package com.hpy.ops360.atmservice.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketRaisedCategoriesByAtmResponseDto {

//	@JsonProperty("id")
//	private Long id;
//
//	@JsonProperty("totalOpenTickets")
//	private String totalOpenTickets;
//
//	@JsonProperty("todaysTicketsColor")
//	private String todaysTicketsColor;
	
	private String title;
    private String value;
    private String color;
}
