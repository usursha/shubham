package com.hpy.ops360.ticketing.cm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TicketCount {

	@Id
	private Long srNo;
	private long openCount;

}
