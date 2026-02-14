package com.hpy.ops360.ticketing.ticket.dto;

import com.hpy.ops360.framework.dto.UserLocationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@EqualsAndHashCode(callSuper=true)
public class BroadCategoryDto2 {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4694058094948871084L;
	private String category;
	private String subcallType;


}
