package com.hpy.ops360.dashboard.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hpy.ops360.dashboard.dto.SiteVisitsDto;
import com.hpy.ops360.dashboard.entity.SiteVisits;
import com.hpy.ops360.dashboard.logapi.Loggable;
import com.hpy.ops360.dashboard.repository.SiteVisitRepository;
import com.hpy.ops360.dashboard.util.Helper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SiteVisitService {

	private final SiteVisitRepository siteVisitRepository;

	private final Helper helper;

	@Loggable
	public List<SiteVisitsDto> getSiteVisitDetails() {
	    List<SiteVisits> siteVisits = siteVisitRepository.getSiteVisitDetails(helper.getLoggedInUser());

	    // Check if the result is empty, if yes return empty list, otherwise map to DTO
	    return siteVisits.isEmpty() ? Collections.emptyList() :
	        siteVisits.stream()
	                  .map(result -> new SiteVisitsDto(
	                      result.getSrNo(),
	                      result.getTicketNumber(),
	                      result.getVisitType(),
	                      result.getSiteCode(),
	                      result.getSiteDesc(),
	                      result.getStartDate(),
	                      result.getEndDate()
	                  )).toList();
	}

}
