package com.hpy.ops360.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class DownTime_FilterResponseDTO {

	@JsonProperty("downTime")
	private String downTime;
	@JsonProperty("min")
	public int Min;
	@JsonProperty("max")
	public int Max;
}
