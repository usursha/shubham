package com.hpy.ops360.ticketing.cm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class TravelEtaDetails {

	@Id
	private Long srNo;
	private String owner;
	private String customerRemark;
	private String internalRemark;
	private String travelEtaDate;
	private String travelEtaTime;
//	@Column(name = "travel_durection")
	private String travelDuration;
	private String travelEtaDateResolve;
	private int travelDurationResolve;
}
