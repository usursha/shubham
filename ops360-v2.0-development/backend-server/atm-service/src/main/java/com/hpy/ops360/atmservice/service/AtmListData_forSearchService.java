package com.hpy.ops360.atmservice.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.dto.AtmListData_forSearchRequestDto;
import com.hpy.ops360.atmservice.dto.AtmListData_forSearchResponseDto;
import com.hpy.ops360.atmservice.dto.LoginService;
import com.hpy.ops360.atmservice.entity.AtmListData_forSearch;
import com.hpy.ops360.atmservice.repository.AtmListData_forSearchRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AtmListData_forSearchService {

	@Autowired
	private AtmListData_forSearchRepository atmListData_forSearchRepository;
	
	@Autowired
	private LoginService loginService;
	
	

	public List<AtmListData_forSearchResponseDto> getAllAtmAgainstFilter(AtmListData_forSearchRequestDto requestDto) {

		String userId = loginService.getLoggedInUser();
		List<AtmListData_forSearch> atmListData_forSearchs = atmListData_forSearchRepository
				.getAllAtmAgainstFilter(userId, requestDto.getBank(), requestDto.getGrade(), requestDto.getStatus(), requestDto.getUptime_status());
		

		return atmListData_forSearchs.stream().map(this::toDto).collect(Collectors.toList());

	}
	
	public AtmListData_forSearchResponseDto toDto(AtmListData_forSearch entity) {
		AtmListData_forSearchResponseDto dto = new AtmListData_forSearchResponseDto();
		dto.setAtmid(entity.getAtmid());
		dto.setBankname(entity.getBankname());
		dto.setGrade(entity.getGrade());
		dto.setAddress(entity.getAddress());
		dto.setMachineStatus(entity.getMachineStatus());
		dto.setUptimeStatus(entity.getUptimeStatus());
		dto.setOpenTickets(entity.getOpenTickets());
		dto.setTransactionTrend(entity.getTransactionTrend());
		dto.setMtdPerformance(entity.getMtdPerformance());
		dto.setUptimeTrend(entity.getUptimeTrend());
		dto.setMtdUptime(entity.getMtdUptime());
		dto.setNameOfChannelExecutive(entity.getNameOfChannelExecutive());
		dto.setNameOfSecondaryChannelExecutive(entity.getNameOfSecondaryChannelExecutive());
		dto.setLastVisitedOn(entity.getLastVisitedOn());
		return dto;
		
	}
}
