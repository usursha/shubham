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
public class OpenTicketsWithCategoryDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
	//private List<Map<String, Object>> ticketData; 
	private List<AtmShortDetailsDto> ticketShortDetails;
	private TicketCategoryCountDto ticketCategoryCount;
	// private TicketsWithCategoryCountDto categoryCount;
	
	@Builder
	public OpenTicketsWithCategoryDto(List<AtmShortDetailsDto> ticketShortDetails, TicketCategoryCountDto ticketCategoryCount) {
		this.ticketShortDetails = ticketShortDetails;
		this.ticketCategoryCount = ticketCategoryCount;
		
	}
//	@Builder
//	public OpenTicketsWithCategoryDto(List<Map<String, Object>> ticketData,List<AtmShortDetailsDto> ticketShortDetails, TicketCategoryCountDto ticketCategoryCount) {
//		this.ticketShortDetails = ticketShortDetails;
//		this.ticketCategoryCount = ticketCategoryCount;
//		this.ticketShortDetails = null;
//	}

	
}
