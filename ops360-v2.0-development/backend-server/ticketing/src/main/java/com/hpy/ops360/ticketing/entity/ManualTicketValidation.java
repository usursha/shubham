package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ManualTicketValidation {
	
	@Id
	@Column(name="sr_no")
	private Long srNo;
	
	@Column(name="code")
	private int code;
	
	@Column(name="message")
	private String message;
}
