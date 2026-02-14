package com.hpy.ops360.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hpy.ops360.framework.dto.UserLocationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemarksrequestDto extends UserLocationDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1887976397547173993L;

	@JsonProperty("ticketno")
	private String ticketno;

	@JsonProperty("atmid")
	private String atmid;

}
