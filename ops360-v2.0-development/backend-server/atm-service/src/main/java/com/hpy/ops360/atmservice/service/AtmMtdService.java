package com.hpy.ops360.atmservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.dto.AtmMtdResDto;
import com.hpy.ops360.atmservice.entity.AtmMtdEntity;
import com.hpy.ops360.atmservice.repository.AtmMtdRepository;
import com.hpy.ops360.atmservice.request.AtmMtdReqDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AtmMtdService {
	
	@Autowired AtmMtdRepository atmMtdRepository;
	
	
	public List<AtmMtdResDto> getAtmMTD(AtmMtdReqDto reqDto) {
		List<AtmMtdEntity> entities = atmMtdRepository.getAtmMTD(reqDto.getAtmId(),reqDto.getTypeDate());

		if (entities == null || entities.isEmpty()) {
			return List.of();
		}
		List<AtmMtdResDto> response = entities.stream().map(entity -> {
			AtmMtdResDto dto = new AtmMtdResDto();
			dto.setSrNo(entity.getSrNo());
			dto.setLastUpdatedAt(entity.getLastUpdatedAt());
			dto.setTargetMtd(entity.getTargetMtd());
			return dto;
		}).collect(Collectors.toList());

		return response;

	}

}
