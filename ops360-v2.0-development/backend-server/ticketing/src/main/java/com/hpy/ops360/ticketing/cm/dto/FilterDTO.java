package com.hpy.ops360.ticketing.cm.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterDTO extends GenericDto{
	
	@JsonIgnore
	private Long id;
	
    private List<TicketTypeDTO> ticketType;
    private List<StatusDto> status;
    private List<CreatedDateDTO> createdDate;
	public FilterDTO(List<TicketTypeDTO> ticketType, List<StatusDto> status, List<CreatedDateDTO> createdDate) {
		super();
		this.ticketType = ticketType;
		this.status = status;
		this.createdDate = createdDate;
	}
    
    
    
}

