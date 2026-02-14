package com.hpy.ops360.ticketing.mapper;

import org.springframework.stereotype.Component;

import com.hpy.generic.impl.GenericMapper;
import com.hpy.ops360.ticketing.dto.TravelDto;
import com.hpy.ops360.ticketing.entity.Travel;

@Component
public class TravelMapper extends GenericMapper<TravelDto, Travel> {

	public TravelMapper() {
		super(TravelDto.class, Travel.class);

	}

}
