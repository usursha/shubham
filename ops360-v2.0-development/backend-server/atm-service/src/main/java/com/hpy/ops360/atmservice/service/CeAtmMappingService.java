package com.hpy.ops360.atmservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.dto.AssignedAtmCeDto;
import com.hpy.ops360.atmservice.dto.AtmMappingDto;
import com.hpy.ops360.atmservice.dto.CeAssignedAtmDto;
import com.hpy.ops360.atmservice.dto.CeAtmMappingDto;
import com.hpy.ops360.atmservice.dto.InActCeAtmMappedDto;
import com.hpy.ops360.atmservice.dto.InActCeAtmMappingDto;
import com.hpy.ops360.atmservice.entity.CeAssignedAtmEntity;
import com.hpy.ops360.atmservice.entity.CeAtmMappingEntity;
import com.hpy.ops360.atmservice.entity.InActCeAtmMappedEntity;
import com.hpy.ops360.atmservice.repository.CeAssignedAtmRepository;
import com.hpy.ops360.atmservice.repository.CeAtmMappingRepository;
import com.hpy.ops360.atmservice.repository.InActCeAtmMappingRepository;

@Service
public class CeAtmMappingService {

	@Autowired
	private CeAtmMappingRepository ceAtmMappingRepository;

	@Autowired
	private CeAssignedAtmRepository ceAssignedAtmRepository;

	@Autowired
	private InActCeAtmMappingRepository inActCeAtmMappingRepository;

	public List<AtmMappingDto> getCeAtmMappingList(CeAtmMappingDto ceAtmMappingDto) {
		List<CeAtmMappingEntity> ceAtmMappingEntity = ceAtmMappingRepository
				.getCeAtmMappingList(ceAtmMappingDto.getCmUserId());
		return ceAtmMappingEntity.stream().map(ceMap -> new AtmMappingDto(ceMap.getSrno(), ceMap.getCeUserId(),
				ceMap.getDisplayName(), ceMap.getAtmCount())).toList();
	}

	public List<AssignedAtmCeDto> getCeAssignedAtmList(CeAssignedAtmDto ceAssignedAtmDto) {

		List<CeAssignedAtmEntity> ceAssignedAtmEntity = ceAssignedAtmRepository
				.getCeAssignedAtmList(ceAssignedAtmDto.getCeUserId());

		return ceAssignedAtmEntity.stream().map(
				mapp -> new AssignedAtmCeDto(mapp.getSrno(), mapp.getAtmId(), mapp.getBankName(), mapp.getSiteName()))
				.toList();
	}

	public List<InActCeAtmMappedDto> updateInActCeAtmMapping(InActCeAtmMappingDto inActCeAtmMappingDto) {

		List<InActCeAtmMappedEntity> inActCeAtmMappedEntity = inActCeAtmMappingRepository.updateInActCeAtmMapping(
				inActCeAtmMappingDto.getActiveCeUserId(), inActCeAtmMappingDto.getInActiveCeUserId(),
				inActCeAtmMappingDto.getMapAtmId());

		return inActCeAtmMappedEntity.stream()
				.map(mapp -> new InActCeAtmMappedDto(mapp.getSrno(), mapp.getMessage(), mapp.getDataMessage()))
				.toList();

	}

}
