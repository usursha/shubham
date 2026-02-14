package com.hpy.ops360.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmUptimeDto {

	private String lastUpdated;
	private Double uptime; // mtd
	private Integer atmCount; // updowntotalAtm
	private String remarks;
}
