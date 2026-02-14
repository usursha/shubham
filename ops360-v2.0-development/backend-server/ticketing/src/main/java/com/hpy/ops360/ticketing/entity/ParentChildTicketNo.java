package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ParentChildTicketNo {
	
	@Id
	@Column(name="srno")
	private Long srNo;
	
	@Column(name="ticketid")
	private String ticketid;

}
