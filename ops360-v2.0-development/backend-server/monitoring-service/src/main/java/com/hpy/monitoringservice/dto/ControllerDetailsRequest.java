package com.hpy.monitoringservice.dto;

import lombok.Data;

@Data
public class ControllerDetailsRequest {
    private String controllerName;
    private String userName;
    private String fromDate;
	private String toDate;

}