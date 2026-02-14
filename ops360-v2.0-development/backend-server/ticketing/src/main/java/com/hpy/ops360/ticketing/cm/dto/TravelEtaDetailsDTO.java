package com.hpy.ops360.ticketing.cm.dto;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TravelEtaDetailsDTO extends GenericDto{

	private String owner;
	private String customerRemark;
	private String internalRemark;
	private String travelEtaDate;
	private String travelEtaTime;
	private String travelDuration;
	private String travelEtaDateResolve;
	private int travelDurationResolve;
}
