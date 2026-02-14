package com.hpy.ops360.ticketing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class RejectionReason {

//	private Long id;
	@Id
	@Column(name = "sr_no")
	@JsonProperty("sr_no")
	private Long sr_no;

	@Column(name = "rejectionreason")
	@JsonProperty("RejectionReason")
	private String rejectionReason;
}
