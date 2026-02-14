package com.hpy.ops360.sampatti.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaderboardMonthDto extends GenericDto{
	
	@JsonIgnore
	private Long id;

	private Long srno;
	
    private String displayMonth;

	public LeaderboardMonthDto(Long srno, String displayMonth) {
		super();
		this.srno = srno;
		this.displayMonth = displayMonth;
	}
    
    

}
