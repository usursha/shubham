package com.hpy.ops360.ticketing.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.ticketing.enums.TicketCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketManagementResponse extends GenericDto{
	
	@JsonIgnore
	private Long id;
	
    private List<TicketCategoryForAllTickets> data;

	public TicketManagementResponse(List<TicketCategoryForAllTickets> data) {
		super();
		this.data = data;
	}
    
    
}