package com.hpy.ops360.synergyservice.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTicketStatusDto {

	private String ticketNo;
	private String atmId;
	private String updatedby;
	private String comment;
	private String documentName;
	private String document;

}
