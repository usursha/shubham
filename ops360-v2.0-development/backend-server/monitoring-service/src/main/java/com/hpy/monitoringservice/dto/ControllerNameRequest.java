package com.hpy.monitoringservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControllerNameRequest {
    private String controllerName;
    private String fromDate;
	private String toDate;

}