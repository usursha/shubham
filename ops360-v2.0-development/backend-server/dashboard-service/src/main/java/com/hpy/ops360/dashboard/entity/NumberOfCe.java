package com.hpy.ops360.dashboard.entity;

import com.hpy.generic.impl.GenericEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NumberOfCe extends GenericEntity {

	@Column(name = "total_no_ce")
	private String totalNoCe;

	@Column(name = "active_ce")
	private String activeCe;

	@Column(name = "inactive_ce")
	private String inactiveCe;

}
