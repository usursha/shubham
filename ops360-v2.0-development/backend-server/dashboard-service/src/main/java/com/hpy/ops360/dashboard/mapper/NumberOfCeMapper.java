package com.hpy.ops360.dashboard.mapper;

import org.springframework.stereotype.Component;

import com.hpy.generic.impl.GenericMapper;
import com.hpy.ops360.dashboard.dto.NumberOfCeDto;
import com.hpy.ops360.dashboard.entity.NumberOfCe;

@Component
public class NumberOfCeMapper extends GenericMapper<NumberOfCeDto, NumberOfCe> {

	public NumberOfCeMapper() {
		super(NumberOfCeDto.class, NumberOfCe.class);
	}

}
