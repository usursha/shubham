package com.hpy.ops360.ticketing.dto;

import com.hpy.ops360.framework.dto.UserLocationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorIdDto extends UserLocationDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6795544887439655419L;
	private String vendor;
	private String atmId;

}
