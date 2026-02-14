package com.hpy.ops360.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.ticketing.entity.TravelModeMessage;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelModeMessageDto extends GenericDto{
	
	@JsonIgnore
	private Long id;

	private Long srNo;

	private String message;

	private String colorCodeHex;

	private String textColourHex;

	private String atmId;

	private String ticketNumber;

	private String travelEtaDatetime;

	private String lastModifiedTime;

	private Integer travelDuration;

	private Integer isDurationCrossed;

	private Integer isTravelling;
}
