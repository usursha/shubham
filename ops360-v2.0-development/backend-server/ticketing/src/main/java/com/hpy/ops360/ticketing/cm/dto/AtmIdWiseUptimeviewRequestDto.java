package com.hpy.ops360.ticketing.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmIdWiseUptimeviewRequestDto {

	private String startDate;
	private String endDate;
	private String atmIds;
	private String status;
	private int page;
	private int pagesize;

}
