package com.hpy.ops360.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TransactionTrendFilterResponseDTO {

	@JsonProperty("value")
	private String value;

	@JsonProperty("min")
	public int Min;
	@JsonProperty("max")
	public int Max;

}
