package com.hpy.ops360.ticketing.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hpy.generic.impl.GenericService;
import com.hpy.ops360.ticketing.dto.TravelModeDto;
import com.hpy.ops360.ticketing.entity.TravelModeEntity;
import com.hpy.ops360.ticketing.repository.TravelModeRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TravelModeService extends GenericService<TravelModeDto, TravelModeEntity> {

	private TravelModeRepository travelModeRepository;

	public List<TravelModeDto> getTravelModes() {

		return this.getMapper().toDto(travelModeRepository.findAll());
	}

	public TravelModeDto addTravelMode(TravelModeDto travelModeDto) {

		return save(travelModeDto);
	}

}
