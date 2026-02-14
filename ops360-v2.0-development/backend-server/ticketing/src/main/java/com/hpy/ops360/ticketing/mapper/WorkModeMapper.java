package com.hpy.ops360.ticketing.mapper;

import org.springframework.stereotype.Component;

import com.hpy.generic.impl.GenericMapper;
import com.hpy.ops360.ticketing.dto.WorkModeDto;
import com.hpy.ops360.ticketing.entity.WorkModeEntity;

@Component
public class WorkModeMapper extends GenericMapper<WorkModeDto, WorkModeEntity> {

	public WorkModeMapper() {
		super(WorkModeDto.class, WorkModeEntity.class);

	}

}
