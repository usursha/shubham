package com.hpy.ops360.ticketing.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmUptimeDto {

	private String lastUpdated;
	private Double uptime;
	private Integer atmCount;
	private String remarks;
}
