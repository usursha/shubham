package com.hpy.ops360.ticketing.dto;

import com.hpy.ops360.framework.dto.UserLocationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCallTypeDto extends UserLocationDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8207016551857531098L;
	private String subcallType;
	private String ticketNumber;
	private String atmId;

}
