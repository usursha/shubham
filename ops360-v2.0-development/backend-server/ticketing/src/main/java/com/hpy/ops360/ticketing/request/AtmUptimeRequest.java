package com.hpy.ops360.ticketing.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AtmUptimeRequest {
	
	private String requestid;
	private String atmid;

}
