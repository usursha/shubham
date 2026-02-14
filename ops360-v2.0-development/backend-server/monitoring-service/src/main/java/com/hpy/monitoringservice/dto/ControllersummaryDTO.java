package com.hpy.monitoringservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControllersummaryDTO {
	private Long srno;
    private String startTime;
    private String endTime;
    private String callCount;
    private String avgTimeTaken;
    private String maxTimeTaken;
    private List<ControllerInternalDTO> controllerDetails;

}