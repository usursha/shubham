package com.hpy.ops360.ticketing.entity;


import java.util.Date;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class SummaryReport {

	 @Id
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
