package com.hpy.ops360.ticketing.cm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ATMTicketsRaisedCount {

	@Id
	@JsonIgnore
	private Long srNo;
	private int breakdown;
	private int service;
	private int updated;
	private int total;
}
