package com.hpy.mappingservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.mappingservice.entity.TemporaryMappingLeaveListEntity;
import com.hpy.mappingservice.repository.TemporaryMappingLeaveListRepository;
import com.hpy.mappingservice.response.dto.TemporaryMappingLeaveListResponseDto;

@Service
public class TemporaryMappingLeaveListService {

	@Autowired
	private LoginService loginService;

	@Autowired
	private TemporaryMappingLeaveListRepository temporaryMappingLeaveListRepository;

	public List<TemporaryMappingLeaveListResponseDto> getUserLeaveDetails() {

		String cmUserId = loginService.getLoggedInUser();
		List<TemporaryMappingLeaveListEntity> results = temporaryMappingLeaveListRepository
				.getUserLeaveDetailsBySP(cmUserId);

		return results.stream().map(this::toDto).collect(Collectors.toList());
	}

	private TemporaryMappingLeaveListResponseDto toDto(TemporaryMappingLeaveListEntity entity) {
		TemporaryMappingLeaveListResponseDto dto = new TemporaryMappingLeaveListResponseDto();
		dto.setCeUserId(entity.getCeUserId());
		dto.setAtmCount(entity.getAtmCount());
		dto.setFullName(entity.getFullName());
		dto.setCustomEndTime(entity.getCustomEndTime());
		dto.setCustomStartTime(entity.getCustomStartTime());
		dto.setPercentage("100");
		return dto;

	}
}
