package com.hpy.ops360.ticketing.mapper;

import org.springframework.stereotype.Component;

import com.hpy.generic.impl.GenericMapper;
import com.hpy.ops360.ticketing.dto.TicketAssetsDto;
import com.hpy.ops360.ticketing.entity.TicketAssets;

@Component
public class TicketAssetsMapper extends GenericMapper<TicketAssetsDto, TicketAssets> {

	public TicketAssetsMapper() {
		super(TicketAssetsDto.class, TicketAssets.class);
		// TODO Auto-generated constructor stub
	}

}
