package com.hpy.monitoringservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControllerInternalDTO {
	
	@JsonProperty("controller_name")
	private String controllerName;

	@JsonProperty("call_count")
	private String callCount;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@JsonProperty("avg_time_taken")
	private double avgTimeTaken;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@JsonProperty("max_time_taken")
    private double maxTimeTaken;
}