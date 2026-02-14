package com.hpy.ops360.ticketing.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hpy.ops360.framework.entity.UserLocation;
import com.hpy.ops360.ticketing.enums.TravelMode;
import com.hpy.ops360.ticketing.enums.WorkMode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "travel")
public class Travel extends UserLocation {

	@Column(name = "ticket_no")
	private String ticketNo;

	@Column(name = "atm_id")
	private String atmId;

	@Column(name = "at_site")
	private Boolean atSite;

	@Column(name = "travelling_to_site")
	private Boolean travellingToSite;

	@Enumerated(EnumType.STRING)
	@Column(name = "work_mode")
	private WorkMode workMode;

	@Enumerated(EnumType.STRING)
	@Column(name = "travel_mode")
	private TravelMode travelMode;

	@JsonFormat(pattern = "dd/MM/yy HH:ss")
	@Column(name = "travel_eta_datetime")
	private LocalDateTime travelEtaDateTime;

	@Column(name = "username")
	private String username;
}
