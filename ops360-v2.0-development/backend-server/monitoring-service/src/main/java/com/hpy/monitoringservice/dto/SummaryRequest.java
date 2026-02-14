package com.hpy.monitoringservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryRequest {

	private int intervalMinutes;
	private int limitRows;
}
