package com.hpy.ops360.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupTicketListResponse extends GenericDto implements Serializable {
	
	
	@JsonIgnore
	private Long id;
	private int total_records;
	
	private List<GroupedTicketResponse> ticket_data;

	public void setTicket_data(List<GroupedTicketResponse> ticketData) {
		// TODO Auto-generated method stub
		this.ticket_data = ticketData;
	}


}
