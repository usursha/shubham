package com.hpy.ops360.ticketing.ticket.dto;

import com.hpy.ops360.ticketing.dto.GenericResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationCountDTO extends GenericResponseDto {

	private Long totalCount;
	private Long ticketCount;
	private Long leaveCount;
//	
}
