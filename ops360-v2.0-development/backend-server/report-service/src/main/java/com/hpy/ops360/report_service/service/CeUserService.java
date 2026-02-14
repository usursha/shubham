package com.hpy.ops360.report_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.report_service.config.Helper;
import com.hpy.ops360.report_service.dto.CeUserDetailsDto;
import com.hpy.ops360.report_service.dto.UptimeRangeDTO;
import com.hpy.ops360.report_service.dto.UptimeResponseFilter;
import com.hpy.ops360.report_service.entity.CeUserDetails;
import com.hpy.ops360.report_service.entity.UptimeRange;
import com.hpy.ops360.report_service.repository.CeUserRepository;
import com.hpy.ops360.report_service.repository.UptimeRangeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CeUserService {

	@Autowired
    private CeUserRepository ceUserRepository;
	
	@Autowired
	private Helper helper;
	
	@Autowired
	private UptimeRangeRepository uptimeRangeRepository;

    public UptimeResponseFilter getCeUsers() {
    	log.info("Inside Service Layer to fetch CE Users");
    	String cmuser= helper.getLoggedInUser();
    	
    	UptimeResponseFilter response=new UptimeResponseFilter();
    	log.info("Fetching CE Users for CM User ID: {}", cmuser);
    	List<CeUserDetails> data=ceUserRepository.getCeUsersByCmUserId(cmuser);
    	//map to DTO
		List<CeUserDetailsDto> rangedata = data.stream()
				.map(ceUser -> new CeUserDetailsDto(ceUser.getCeUserId(), ceUser.getCeName(), ceUser.getCeEmail(),
						ceUser.getCeMobile()))
				.toList();
		
		List<UptimeRange> uptimedata=  uptimeRangeRepository.findAll();
		//map to dto UptimeRangeDTO
		List<UptimeRangeDTO> uptimeRangeDTOs = uptimedata.stream()
				.map(range -> new UptimeRangeDTO(range.getId(), range.getName(), range.getRange()))
				.toList();
		
	    response.setUserDetails(rangedata);
	    response.setUptimeRanges(uptimeRangeDTOs);
	    response.setUptimeachievedRanges(uptimeRangeDTOs);
	    response.setTxnRanges(uptimeRangeDTOs);
	    response.setTxnachievedRanges(uptimeRangeDTOs);
	    log.info("Successfully fetched CE Users and Uptime Ranges");
		
        return response;
    }
}
