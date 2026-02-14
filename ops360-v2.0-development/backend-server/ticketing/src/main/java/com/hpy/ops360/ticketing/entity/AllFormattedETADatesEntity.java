package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AllFormattedETADatesEntity {
	@Id
	private String srno;
	
	@Column(name="formatted_eta_date_time")
	private String formattedEtaDateTime;

}
