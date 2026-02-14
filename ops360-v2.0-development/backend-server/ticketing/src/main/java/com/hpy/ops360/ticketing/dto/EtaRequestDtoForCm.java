package com.hpy.ops360.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EtaRequestDtoForCm {

	private String ticketNumber;

	private String atmid;

	private String owner;

	private String subcallType;

	private String internalRemark;
	
	private String customerRemark;

}
