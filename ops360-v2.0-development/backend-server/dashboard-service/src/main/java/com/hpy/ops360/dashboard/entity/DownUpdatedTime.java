package com.hpy.ops360.dashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "temp_down_updated_time")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownUpdatedTime {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "sr_no")
	private Long srNo;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "atm_id")
	private String atmId;

	@Column(name = "ticket_id")
	private String ticketId;

}
