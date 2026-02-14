package com.hpy.ops360.report_service.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ATMUptimeFilterResponse extends GenericDto {
	@JsonIgnore
	private Long id;
	
	private List<UptimeRangeDTO> uptimeRanges;
	private List<StatusDto> status;
    private List<AtmidDto> atmid;
    private List<BankDto> bank;
    private List<AtmidDto> PSId;
    private List<SiteTypeDto> sitetype;
    private List<CityDto> city;
    private List<String> sortbyfield;

}
