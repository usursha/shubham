package com.hpy.ops360.dashboard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmUpDownCount {

	@Id
	@Column(name = "sr_no")
	@JsonIgnore
	private Long srNo;

	@Column(name = "DOWN_ATM")
	private int downAtm;

	@Column(name = "UP_ATM")
	private int upAtm;

	@Column(name = "TOTAL_ATM")
	private int totalAtm;
}
