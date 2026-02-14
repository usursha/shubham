package com.hpy.ops360.synergyservice.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AtmUptimeRequest {
	
	private String requestid;
	private String atmid;

}
