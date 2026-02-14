package com.hpy.ops360.ticketing.dto;

import com.hpy.ops360.framework.dto.UserLocationDto;
import com.hpy.ops360.ticketing.enums.TravelMode;
import com.hpy.ops360.ticketing.enums.WorkMode;

import lombok.Data;

@Data
public class TravelDto extends UserLocationDto {

	private static final long serialVersionUID = -3754484367703631646L;
	private String ticketNo;
	private String atmId;
//	private String bank;
//	private String siteName;
//	private String owner;
//	private String vendor;
//	private String error;
//	private Integer remainingTime;
	private Boolean atSite;
	private Boolean travellingToSite;
	private WorkMode workMode;
	private TravelMode travelMode;
	private String travelEtaDateTime;
	private String username;

}
