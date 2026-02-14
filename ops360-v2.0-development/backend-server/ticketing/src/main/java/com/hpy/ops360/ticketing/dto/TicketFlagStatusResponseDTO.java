package com.hpy.ops360.ticketing.dto;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketFlagStatusResponseDTO extends GenericDto {

	private int flagStatus;
}
