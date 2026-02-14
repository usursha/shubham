package com.hpy.ops360.atmservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchBankLocationDto extends GenericDto{

	@JsonIgnore
	private Long id;
	
	private String bank;
	private String location;
	public SearchBankLocationDto(String bank, String location) {
		super();
		this.bank = bank;
		this.location = location;
	}

	
}
