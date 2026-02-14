package com.hpy.ops360.ticketing.mapper;

import org.springframework.stereotype.Component;

import com.hpy.generic.impl.GenericMapper;
import com.hpy.ops360.ticketing.dto.TravelModeDto;
import com.hpy.ops360.ticketing.entity.TravelModeEntity;

@Component
public class TravelModeMapper extends GenericMapper<TravelModeDto, TravelModeEntity> {

	public TravelModeMapper() {
		super(TravelModeDto.class, TravelModeEntity.class);

	}

}
