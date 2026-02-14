package com.hpy.ops360.atmservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.atmservice.ticket.dto.TicketCategoryCountDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class AssignedAtmDetailsDto extends GenericDto{

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private Long id;

	private String atmId;
	private String address;
	private double mtdUptime;
	private String lastestticketNo;
	private String lastVisited;
	private int isBreakdownCount;
	private String machineMaxDowntime;
	TicketCategoryCountDto ticketCategoryCount;
	List<NTicketHistory> ticketHistories;
	public AssignedAtmDetailsDto(String atmId, String address, double mtdUptime, String lastestticketNo,
			String lastVisited, int isBreakdownCount, String machineMaxDowntime,
			TicketCategoryCountDto ticketCategoryCount, List<NTicketHistory> ticketHistories) {
		super();
		this.atmId = atmId;
		this.address = address;
		this.mtdUptime = mtdUptime;
		this.lastestticketNo = lastestticketNo;
		this.lastVisited = lastVisited;
		this.isBreakdownCount = isBreakdownCount;
		this.machineMaxDowntime = machineMaxDowntime;
		this.ticketCategoryCount = ticketCategoryCount;
		this.ticketHistories = ticketHistories;
	}
	
	
	
}
