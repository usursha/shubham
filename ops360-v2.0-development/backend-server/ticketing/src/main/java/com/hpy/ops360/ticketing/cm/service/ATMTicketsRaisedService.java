package com.hpy.ops360.ticketing.cm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.ATMTicketsRaisedCountDto;
import com.hpy.ops360.ticketing.cm.dto.ATMTicketsRaisedRequestDTO;
import com.hpy.ops360.ticketing.cm.entity.ATMTicketsRaisedCount;
import com.hpy.ops360.ticketing.cm.repo.ATMTicketsRaisedRepository;

@Service
public class ATMTicketsRaisedService {

	@Autowired
	private ATMTicketsRaisedRepository atmTicketsRepository;

	public ATMTicketsRaisedCountDto getATMTicketsRaisedCount(ATMTicketsRaisedRequestDTO requestDTO) {
		ATMTicketsRaisedCount count=atmTicketsRepository.getATMTicketsCount(requestDTO.getAtmId());
		return getTicketsRaisedCountResponseMapper(count);
	}
	
	private ATMTicketsRaisedCountDto getTicketsRaisedCountResponseMapper(ATMTicketsRaisedCount count) {
		ATMTicketsRaisedCountDto countresponseDto = new ATMTicketsRaisedCountDto();
		countresponseDto.setBreakdown(count.getBreakdown());
		countresponseDto.setService(count.getService());
		countresponseDto.setTotal(count.getTotal());
		countresponseDto.setUpdated(count.getUpdated());
		return countresponseDto;

	}
	
	

}
