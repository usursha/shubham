package com.hpy.ops360.atmservice.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketCategoryCountDto {

	private Integer total;
	private Integer cash;
	private Integer communication;
	private Integer hardwareFault;
	private Integer supervisory;
	private Integer others;

}
