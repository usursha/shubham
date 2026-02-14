package com.hpy.ops360.ticketing.mapper;

import org.springframework.stereotype.Component;

import com.hpy.generic.impl.GenericMapper;
import com.hpy.ops360.ticketing.dto.TicketDto;
import com.hpy.ops360.ticketing.entity.Ticket;

@Component
public class TicketMapper extends GenericMapper<TicketDto, Ticket> {

	public TicketMapper() {
		super(TicketDto.class, Ticket.class);
		// TODO Auto-generated constructor stub
	}

}
