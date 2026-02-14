package com.hpy.ops360.ticketing.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hpy.ops360.ticketing.dto.AtmDetailsWithSourceDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OpenTicketsRequestHims {

	private String requestid;
	@JsonProperty("atmlist")
	private List<AtmDetailsWithSourceDto> atmdetails;
}
