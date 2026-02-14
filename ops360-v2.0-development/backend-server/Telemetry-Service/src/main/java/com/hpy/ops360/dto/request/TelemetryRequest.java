package com.hpy.ops360.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelemetryRequest {

	private String journeyId;
	private String userName;
	private String screen;
	private String action;
	private String deviceModel;
	private String osVersion;
	private String insertDateTime;
}
