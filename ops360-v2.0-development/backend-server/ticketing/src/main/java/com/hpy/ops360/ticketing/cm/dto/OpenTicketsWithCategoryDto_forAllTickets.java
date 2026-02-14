package com.hpy.ops360.ticketing.cm.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.ticketing.dto.TicketCategoryCountDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenTicketsWithCategoryDto_forAllTickets extends GenericDto {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private Long id;
	private List<Map<String, Object>> flagticketData;
	private List<Map<String, Object>> otherticketData;
	//private List<AtmShortDetailsDto> ticketShortDetails;
	//private TicketCategoryCountDto ticketCategoryCount;
	//private TicketCategoryCountDto categoryCountDto;
	private com.hpy.ops360.ticketing.dto.TicketCategoryCountDto ticketCategoryCountDto;
	@Builder
	public OpenTicketsWithCategoryDto_forAllTickets(List<Map<String, Object>> flagticketData,
			List<Map<String, Object>> othersticketData, List<AtmShortDetailsDto> ticketShortDetails,
			com.hpy.ops360.ticketing.dto.TicketCategoryCountDto ticketCategoryCount) {
		super();
		this.flagticketData = flagticketData;
		this.otherticketData = othersticketData;
		//this.ticketShortDetails = ticketShortDetails;
		this.ticketCategoryCountDto = ticketCategoryCount;
	}

	
	//@Builder
   
	

}