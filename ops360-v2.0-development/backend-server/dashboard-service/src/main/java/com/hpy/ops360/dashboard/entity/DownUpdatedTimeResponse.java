package com.hpy.ops360.dashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
//@Table(name = "GetDownUpdatedTime_1")
@NoArgsConstructor
@AllArgsConstructor
public class DownUpdatedTimeResponse {

	@Id
	@Column(name = "sr_no")
	private Long srNo;

	@Column(name = "atm_id")
	private String atmId;

	@Column(name = "ticket_id")
	private String ticketId;

	@Column(name = "is_updated")
	private Integer isUpdated;

	@Column(name = "is_timed_out")
	private Integer isTimedOut;

	@Column(name = "is_travelling")
	private Integer isTravelling;

}
