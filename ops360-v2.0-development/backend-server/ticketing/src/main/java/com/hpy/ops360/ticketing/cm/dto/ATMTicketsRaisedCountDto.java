package com.hpy.ops360.ticketing.cm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ATMTicketsRaisedCountDto extends GenericDto {

	@JsonIgnore
	private Long id;
		
	
		@Id
		@JsonIgnore
		private int sr_no;
		
		private int breakdown;
		private int service;
		private int updated;
		private int total;
	
}
