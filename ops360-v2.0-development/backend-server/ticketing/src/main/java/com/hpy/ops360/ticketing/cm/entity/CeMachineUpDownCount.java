package com.hpy.ops360.ticketing.cm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class CeMachineUpDownCount {

	@Id
	@Column(name = "sr_no")
	private Long srNo;

	@Column(name = "DOWN_ATM")
	private int downAtm;

	@Column(name = "UP_ATM")
	private int upAtm;

	@Column(name = "TOTAL_ATM")
	private int totalAtm;

}
