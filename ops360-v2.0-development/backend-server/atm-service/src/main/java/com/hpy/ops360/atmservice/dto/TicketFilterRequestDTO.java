package com.hpy.ops360.atmservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketFilterRequestDTO {

	private String cmUserId;
	private String pageSize;
	private String pageNumber;
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