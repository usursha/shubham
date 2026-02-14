package com.hpy.ops360.dashboard.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.dashboard.entity.AtmIndent;
import com.hpy.ops360.dashboard.entity.SiteVisits;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDataDto extends GenericDto{
	
	@JsonIgnore
	private Long id;

	private CeAtmUptimeDto currentUptime;
	private AtmStatusDto atmStatusDto;
	private TargetMtdSummaryDto targetMtd;

	private List<AtmShortDetailsDto> downCallList;
	private List<SiteVisitsDto> siteVisitList;
	private List<AtmShortDetailsDto> timedOutList;
	private List<AtmShortDetailsDto> updatedList;
	private List<AtmIndent> atmCashIndentList;

}
