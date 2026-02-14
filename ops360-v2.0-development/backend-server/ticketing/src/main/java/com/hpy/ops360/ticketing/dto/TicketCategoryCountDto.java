package com.hpy.ops360.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class TicketCategoryCountDto {

	private Integer total;
	private Integer cash;
	private Integer communication;
	private Integer hardwareFault;
	private Integer noTransactions;
	private Integer supervisory;
	private Integer others;

}
