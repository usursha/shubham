package com.hpy.ops360.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TelemetryAllResponse {

	private String journeyId;
	private String userName;
	private String screen;
	private String action;
	private String deviceModel;
	private String osVersion;
	private String insertDateTime;
}
