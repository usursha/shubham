package com.hpy.sampatti_data_service.response;

import lombok.Data;

@Data
public class DashboardDataDto {
	
	private UserDetailsDto userDetails;
	private ActualDataDto actualData;
	private TrendDto trend;
	private ActionsRequired actionsRequired;
	private TargetDataDto targetData;

}
