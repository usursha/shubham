package com.hpy.ops360.ticketing.mapper;

import org.springframework.stereotype.Component;

import com.hpy.generic.impl.GenericMapper;
import com.hpy.ops360.ticketing.dto.RemarkDto;
import com.hpy.ops360.ticketing.entity.Remark;
import com.hpy.ops360.ticketing.entity.Ticket;

@Component
public class RemarkMapper extends GenericMapper<RemarkDto, Remark> {

	public RemarkMapper() {
		super(RemarkDto.class, Remark.class);
	}

	@Override
	public Remark toEntity(RemarkDto dto) {

		Remark remark = super.toEntity(dto);
		Ticket ticket = new Ticket();
		ticket.setId(dto.getTicketId());
		remark.setTicket(ticket);
		if (dto.getParentRemark() != null) {
			remark.setParentRemark(toEntity(dto.getParentRemark()));
		}
		return remark;
	}

	@Override
	public RemarkDto toDto(Remark entity) {
		RemarkDto dto = super.toDto(entity);

		dto.setTicketId(entity.getTicket().getId());
		if (entity.getParentRemark() != null) {
			dto.setParentRemark(toDto(entity.getParentRemark()));
		}
		return dto;
	}

}
