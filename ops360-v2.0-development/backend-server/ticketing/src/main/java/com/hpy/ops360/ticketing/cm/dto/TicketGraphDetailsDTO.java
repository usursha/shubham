package com.hpy.ops360.ticketing.cm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class TicketGraphDetailsDTO{
	
	
	@JsonIgnore
	private Long id;
	
	 private String type;
	 private String colore;
	 private int count;

	 public TicketGraphDetailsDTO(String type, String colore, int count) {
		super();
		this.type = type;
		this.colore = colore;
		this.count = count;
	}
    
	 
}