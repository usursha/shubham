package com.hpy.ops360.ticketing.cm.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class CreationdetailsEntity {
	
	@Id
	@Column(name = "srno")
	private String srno;

	@Column(name = "ticketno")
	private String ticketNumber;
	
	@Column(name = "Created_By")
	private String createdBy;
	
	@Column(name = "Created_Time")
	private String createdTime;

}
