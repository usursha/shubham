package com.hpy.ops360.ticketing.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadFileDto {
	private String createdBy;
	private String ticketNumber;
	private String atmId;
	private int index;

}
