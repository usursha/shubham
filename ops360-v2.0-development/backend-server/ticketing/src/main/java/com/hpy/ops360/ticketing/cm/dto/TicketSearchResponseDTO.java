package com.hpy.ops360.ticketing.cm.dto;

import java.util.List;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketSearchResponseDTO extends GenericDto {

	private List<SearchTicketsDto> data;
}
