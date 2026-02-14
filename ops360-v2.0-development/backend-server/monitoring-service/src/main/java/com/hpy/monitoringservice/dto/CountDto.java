package com.hpy.monitoringservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountDto {

	private int ceLoggedInCount;
	private int cmLoggedInCount;
	private int totalCeCount;
	private int totalCmCount;
	private int totalUserCount;
}
