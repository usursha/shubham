package com.hpy.ops360.atmservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.entity.AtmDetails;
import com.hpy.ops360.atmservice.repository.AtmDetailsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AtmDetailsFromSpService {

	@Autowired
	private AtmDetailsRepository atmDetailsRepository;

	public AtmDetails getAtmDetailsFromSp(String atmId) {
		// needs testing
		log.info("AtmDetailsFromSpService|getAtmDetailsFromSp|atmId:{}", atmId);
		AtmDetails atmDetails = atmDetailsRepository.getAtmDetails(atmId);
		log.info("AtmDetailsFromSpService|getAtmDetailsFromSp|atmDetails:{}", atmDetails);
		return atmDetails;
	}

}
