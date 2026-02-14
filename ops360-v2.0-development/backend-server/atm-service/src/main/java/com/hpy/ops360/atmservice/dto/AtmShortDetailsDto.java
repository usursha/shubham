package com.hpy.ops360.atmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AtmShortDetailsDto {

	private String atmId;
	private String ticketNumber;
	private String bank;
	private String siteName;
	private String owner;
	private String vendor;

	private String error;
	private String downTime;
}
