package com.hpy.ops360.ticketing.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.dto.TicketAtmLocationDetailsReqDto;
import com.hpy.ops360.ticketing.dto.TicketAtmLocationDetailsResDto;
import com.hpy.ops360.ticketing.entity.TicketAtmLocationDetailsEntity;
import com.hpy.ops360.ticketing.repository.TicketAtmLocationDetailsRepository;

@Service
public class TicketAtmLocationDetailsService {
	
	
	@Autowired
	private TicketAtmLocationDetailsRepository ticketAtmLocationDetailsRepository;
	
	
	public TicketAtmLocationDetailsResDto getTicketAtmLocationDetails(TicketAtmLocationDetailsReqDto reqDto) {
	    List<TicketAtmLocationDetailsEntity> entities = ticketAtmLocationDetailsRepository.getTicketAtmLocationDetails(
	        reqDto.getUserName(),reqDto.getTicketNumber(), reqDto.getAtmId());
	 
	    if (entities == null || entities.isEmpty()) {
	        return null; 
	    }
	 
	    TicketAtmLocationDetailsEntity entity = entities.get(0); 
	    TicketAtmLocationDetailsResDto dto = new TicketAtmLocationDetailsResDto();
	    dto.setSrNo(entity.getSrNo());
	    dto.setAtmId(entity.getAtmId());
	    dto.setBankName(entity.getBankName());
	    dto.setModel(entity.getModel());
	    dto.setWarranty(entity.getWarranty());
	    dto.setSiteId(entity.getSiteId());
	    dto.setOtherAtms(entity.getOtherAtms());
	    dto.setAddress(entity.getAddress());
	    dto.setLastVisited(entity.getLastVisited());
	 
	    return dto;
	}

}
