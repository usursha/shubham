package com.hpy.ops360.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortalWorkMetricsDto {
	
	private String employeeId;
	private String dateOfJoining;
	private String area;
	private String workEmailAddress;
	private Long AtmAssigned;
	private Long ChannelExecutivesAssigned;

}
