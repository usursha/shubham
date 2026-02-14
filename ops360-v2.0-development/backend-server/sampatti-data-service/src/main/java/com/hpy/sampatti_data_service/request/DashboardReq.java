package com.hpy.sampatti_data_service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardReq {
	
	private String userId;
	private String token;
	private String sessionID;
	private String requestTimestamp;
	private String requestType;

}
