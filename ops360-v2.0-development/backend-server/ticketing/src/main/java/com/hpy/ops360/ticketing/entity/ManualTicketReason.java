package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ManualTicketReason {

	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "reason")
	private String reason;

}
