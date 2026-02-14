package com.hpy.ops360.ticketing.request;

import com.hpy.ops360.framework.dto.UserLocationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentCallReq extends UserLocationDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3377022725440852725L;
	private String agentContactNumber;	
}
