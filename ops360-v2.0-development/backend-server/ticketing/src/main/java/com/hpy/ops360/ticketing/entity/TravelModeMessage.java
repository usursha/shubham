package com.hpy.ops360.ticketing.entity;

import com.hpy.generic.impl.GenericDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelModeMessage {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "sr_no")
	private Long srNo;

	@Column(name = "Message")
	private String message;

	@Column(name = "colour_code_hex")
	private String colorCodeHex;

	@Column(name = "text_colour_hex")
	private String textColourHex;

	@Column(name = "atm_id")
	private String atmId;

	@Column(name = "ticket_no")
	private String ticketNumber;

	@Column(name = "travel_eta_datetime")
	private String travelEtaDatetime;

	@Column(name = "last_modified_time")
	private String lastModifiedTime;

	@Column(name = "travel_duration")
	private Integer travelDuration;

	@Column(name = "is_duration_crossed")
	private Integer isDurationCrossed;

	@Column(name = "is_travelling")
	private Integer isTravelling;

}
