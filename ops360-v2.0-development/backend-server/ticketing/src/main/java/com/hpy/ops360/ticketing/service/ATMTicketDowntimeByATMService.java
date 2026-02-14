package com.hpy.ops360.ticketing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.dto.TicketDowntimeByATMReqDto;
import com.hpy.ops360.ticketing.dto.TicketDowntimeByATMResDto;
import com.hpy.ops360.ticketing.entity.TicketDowntimeByATMEntity;
import com.hpy.ops360.ticketing.repository.ATMTicketDowntimeByATMRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ATMTicketDowntimeByATMService {
	
	
	@Autowired
	private ATMTicketDowntimeByATMRepository atmTicketDowntimeByATMRepository;
	 
	public TicketDowntimeByATMResDto getATMTicketDowntimeByATM(TicketDowntimeByATMReqDto reqDto) {
		List<TicketDowntimeByATMEntity> entities = atmTicketDowntimeByATMRepository.getATMTicketDowntimeByATM(reqDto.getAtmId(),reqDto.getTicketNumber());
		if (entities == null || entities.isEmpty()) {
			return null;
		}
		TicketDowntimeByATMEntity entity = entities.get(0);
		TicketDowntimeByATMResDto dto = new TicketDowntimeByATMResDto();
			dto.setSrNo(entity.getSrNo());
			dto.setCreatedDate(entity.getCreatedDate());
			dto.setDuration(entity.getDuration());
			return dto;
		
		}

}
