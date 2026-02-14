package com.hpy.ops360.ticketing.entity;

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
public class AtmDetailsFilterForCe {

	@Id
	@Column(name = "sr_no")
	private Long srNo;

	// @Column(name = "atm_id")
	private String ATMID;

}
