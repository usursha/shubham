package com.hpy.ops360.dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.generic.impl.GenericService;
import com.hpy.ops360.dashboard.dto.NumberOfCeDto;
import com.hpy.ops360.dashboard.entity.NumberOfCe;
import com.hpy.ops360.dashboard.repository.NumberOfCeRepository;

@Service
public class NumberOfCeService extends GenericService<NumberOfCeDto, NumberOfCe> {

	@Autowired
	private NumberOfCeRepository numberOfCeRepository;

	public NumberOfCeDto getCMAgainstCEDetailsCount(String userId) {
		return getMapper().toDto(numberOfCeRepository.ListgetCMAgainstCEDetailsCount(userId));
	}
}
