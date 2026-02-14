package com.hpy.sampatti_data_service.response;

import lombok.Data;

@Data
public class DashboardDataResp {
	
	private String userId;
	private String requestType;
	private String timeStamp;
	private String responseCode;
	private String status;
	private String responseDescription;
	private DashboardDataDto data;

}
