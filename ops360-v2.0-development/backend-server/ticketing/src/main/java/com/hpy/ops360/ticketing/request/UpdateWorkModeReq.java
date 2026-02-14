package com.hpy.ops360.ticketing.request;

import com.hpy.ops360.framework.dto.UserLocationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWorkModeReq extends UserLocationDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 881151356663498520L;
	private String atmId;
	private String ticketNo;
	private String workMode;

}
