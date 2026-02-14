package com.hpy.ops360.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FlagStatusforCynopsysResponseDto extends GenericDto{

	@JsonIgnore
	private Long id;
	
	private int flagStatus;
    private int flaggedTicketsCount;
    
    public FlagStatusforCynopsysResponseDto(int flagStatus, int flaggedTicketsCount) {
		this.flagStatus = flagStatus;
		this.flaggedTicketsCount = flaggedTicketsCount;
	}
}
