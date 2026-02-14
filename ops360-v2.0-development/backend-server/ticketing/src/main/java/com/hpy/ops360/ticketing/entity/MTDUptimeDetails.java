package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class MTDUptimeDetails {
	
	@Id
	private Long srno;
	
	private String atmid;
	
	private double mtd;
	
	private double lastmonthuptime;
}
