package com.hpy.ops360.dashboard.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.dashboard.dto.CEAtmDetailsDtoFor_Cm;
import com.hpy.ops360.dashboard.dto.CeAtmUptimeDto;
import com.hpy.ops360.dashboard.dto.UsersAtmDetailsDto;
import com.hpy.ops360.dashboard.entity.CEAtmDetailsFor_Cm;
import com.hpy.ops360.dashboard.logapi.Loggable;
import com.hpy.ops360.dashboard.repository.CEAtmDetailsFor_Cm_Repository;
import com.hpy.ops360.dashboard.util.DateUtil;

@Service
public class CEAtmDetailsServiceFor_Cm {

	@Autowired
	private CEAtmDetailsFor_Cm_Repository atmDetailsFor_Cm_Repository;

	@Autowired
	private MtdUptimeService mtdUptimeService;

	@Autowired
	private UserAtmDetailsService userAtmDetailsService;

	public List<CEAtmDetailsDtoFor_Cm> getCEAtmDetails(String userId) {
		List<CEAtmDetailsFor_Cm> ceAtmDetailsList = atmDetailsFor_Cm_Repository.getCEAtmDetailsByUserId(userId);
		return ceAtmDetailsList.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	private CEAtmDetailsDtoFor_Cm convertToDto(CEAtmDetailsFor_Cm entity) {
		CEAtmDetailsDtoFor_Cm dto = new CEAtmDetailsDtoFor_Cm();
		dto.setAtmCode(entity.getAtmCode());
		return dto;
	}

	@Loggable
	public CeAtmUptimeDto getMtdUptimeFromSp(String username) {
		List<UsersAtmDetailsDto> userAtmDetails = userAtmDetailsService.getUserAtmDetails(username);
		int totalAtms = 0;
		if (!userAtmDetails.isEmpty()) {
			totalAtms = userAtmDetails.size();
		}

		 return new CeAtmUptimeDto(
			        DateUtil.formatPreviousDayTimestamp(), 
			        mtdUptimeService.getMtdUptimeFromSp(username).getUptime(), 
			        totalAtms, 
			        ""
			    );
	}
}
