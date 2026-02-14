package com.hpy.ops360.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEtaDto {
	private String ticketid;
	private String atmid;
	private String etadatetime;
	private String updatedby;
	private String comment;
}
