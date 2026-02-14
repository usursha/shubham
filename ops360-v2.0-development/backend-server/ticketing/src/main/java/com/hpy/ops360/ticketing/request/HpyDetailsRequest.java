package com.hpy.ops360.ticketing.request;

import com.hpy.ops360.framework.dto.UserLocationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HpyDetailsRequest extends UserLocationDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -204574395416443890L;
	private String atmId;
	private int level;
	private String hpyName;
}
