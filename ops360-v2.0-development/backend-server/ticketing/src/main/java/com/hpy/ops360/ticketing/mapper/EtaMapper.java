package com.hpy.ops360.ticketing.mapper;

import org.springframework.stereotype.Component;

import com.hpy.generic.impl.GenericMapper;
import com.hpy.ops360.ticketing.dto.EtaDto;
import com.hpy.ops360.ticketing.entity.Eta;


@Component
public class EtaMapper extends GenericMapper<EtaDto, Eta> {

	public EtaMapper() {
		super(EtaDto.class, Eta.class);
		
	}
	
	

}
