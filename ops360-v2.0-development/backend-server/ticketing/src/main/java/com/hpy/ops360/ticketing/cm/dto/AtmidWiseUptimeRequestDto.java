package com.hpy.ops360.ticketing.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtmidWiseUptimeRequestDto {

	private String startDate;
	private String endDate;
	private String atmIds;
	private String status;

}
