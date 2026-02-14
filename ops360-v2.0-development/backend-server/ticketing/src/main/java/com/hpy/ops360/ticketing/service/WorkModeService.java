package com.hpy.ops360.ticketing.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hpy.generic.impl.GenericService;
import com.hpy.ops360.ticketing.dto.WorkModeDto;
import com.hpy.ops360.ticketing.entity.WorkModeEntity;
import com.hpy.ops360.ticketing.repository.WorkModeRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WorkModeService extends GenericService<WorkModeDto, WorkModeEntity> {

	private WorkModeRepository workModeRepository;

	public List<WorkModeDto> getWorkModes() {

		return this.getMapper().toDto(workModeRepository.findAll());
	}

	public WorkModeDto addWorkMode(WorkModeDto workModeDto) {

		return save(workModeDto);
	}

}
