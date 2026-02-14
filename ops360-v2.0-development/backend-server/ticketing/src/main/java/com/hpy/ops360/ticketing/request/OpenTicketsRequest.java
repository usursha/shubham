package com.hpy.ops360.ticketing.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hpy.ops360.ticketing.dto.AtmDetailsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenTicketsRequest {

	private String requestid;
	@JsonProperty("atmlist")
	private List<AtmDetailsDto> atmdetails;
}
