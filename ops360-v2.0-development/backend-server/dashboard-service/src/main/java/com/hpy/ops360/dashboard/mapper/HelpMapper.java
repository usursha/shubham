package com.hpy.ops360.dashboard.mapper;

import org.springframework.stereotype.Component;

import com.hpy.generic.impl.GenericMapper;
import com.hpy.ops360.dashboard.dto.HelpDto;
import com.hpy.ops360.dashboard.entity.Help;

@Component
public class HelpMapper extends GenericMapper<HelpDto, Help> {

	public HelpMapper() {
		super(HelpDto.class, Help.class);

	}

}
