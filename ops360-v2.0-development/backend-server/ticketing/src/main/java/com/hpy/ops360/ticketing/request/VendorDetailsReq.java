package com.hpy.ops360.ticketing.request;

import com.hpy.ops360.framework.dto.UserLocationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorDetailsReq extends UserLocationDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5941282888094088194L;
	private String vendor;
	private String atmId;
	private String vendorName;

}
