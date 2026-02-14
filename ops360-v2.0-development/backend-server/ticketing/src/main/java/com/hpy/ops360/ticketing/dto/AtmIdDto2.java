package com.hpy.ops360.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmIdDto2 {

	@JsonProperty("atmId")
	private String atmId;

}
