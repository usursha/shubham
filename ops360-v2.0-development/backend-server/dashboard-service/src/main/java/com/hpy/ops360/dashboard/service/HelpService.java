package com.hpy.ops360.dashboard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hpy.generic.impl.GenericService;
import com.hpy.ops360.dashboard.dto.HelpDto;
import com.hpy.ops360.dashboard.entity.Help;
import com.hpy.ops360.dashboard.mapper.HelpMapper;
import com.hpy.ops360.dashboard.repository.HelpRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HelpService extends GenericService<HelpDto, Help> {

	private final HelpMapper helpMapper;

	private final HelpRepository helpRepository;

	public List<HelpDto> getHelplineDetails() {

		List<Help> helplineDetails = helpRepository.findAll();
		return helpMapper.toDto(helplineDetails);
	}

	public HelpDto addHelplineDetails(HelpDto helpDto) {
		return save(helpDto);

	}

}
