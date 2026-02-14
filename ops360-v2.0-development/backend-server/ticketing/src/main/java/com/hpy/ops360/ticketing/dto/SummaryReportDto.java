package com.hpy.ops360.ticketing.dto;


import java.util.Date;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryReportDto {

	 private int id;
	 private String atmId;
	 private String ticketId;
	 private String eventCode;
	 private String ceName;
	 private Date firstFetchTime;
	 private Date lastFetchTime;
	 private int ticketFetchCount;
	 private int ceUpdateCount;
}
