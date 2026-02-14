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
public class UptimeResponseFilter extends GenericDto{
	
	@JsonIgnore
	private Long id;
	private List<CeUserDetailsDto> userDetails;
	
	private List<UptimeRangeDTO> uptimeRanges;
	private List<UptimeRangeDTO> uptimeachievedRanges;
	private List<UptimeRangeDTO> txnRanges;
	private List<UptimeRangeDTO> txnachievedRanges;

}
