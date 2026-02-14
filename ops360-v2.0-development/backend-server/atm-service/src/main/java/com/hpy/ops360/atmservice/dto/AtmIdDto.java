package com.hpy.ops360.atmservice.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmIdDto extends GenericDto implements Serializable {

	@JsonIgnore
	private Long id;	
	private static final long serialVersionUID = -4542710427634533107L;

	@JsonProperty("atmid")
	private String atmId;

	public AtmIdDto(String atmId) {
		super();
		this.atmId = atmId;
	}
	
	

}
