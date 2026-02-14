package com.hpy.ops360.atmservice.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hpy.ops360.atmservice.dto.AtmIdDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenTicketsRequest {

	private String requestid;

	@JsonProperty("atmlist")
	private List<AtmIdDto> atmdetails;
}
