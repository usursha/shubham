package com.hpy.ops360.atmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TicketSearchRequestDto {

	private String cmUserId;

	private String searchText;

	private String atmId;

	private String sort;

	private String category;

	private String owner;

	private String subCallType;

	private String status;

	private String vendor;

	// Ticket Aging Parameters
	private String ticketAgingHr;

	private String ticketAgingHrFrom;

	private String ticketAgingHrTo;

	private String ticketAgingDay;

	private String ticketAgingDayStart;

	private String ticketAgingDayEnd;

	// Creation Date Parameters
	private String creationDate;

	private String creationDateFrom;

	private String creationDateTo;

}