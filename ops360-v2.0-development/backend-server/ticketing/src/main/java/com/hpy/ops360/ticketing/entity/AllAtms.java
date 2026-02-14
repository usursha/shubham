package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AllAtms {
	
	@Id
	@Column(name="srno")
	private Long srno;
	
	@Column(name="atm_id")
	private String atmId;

}
