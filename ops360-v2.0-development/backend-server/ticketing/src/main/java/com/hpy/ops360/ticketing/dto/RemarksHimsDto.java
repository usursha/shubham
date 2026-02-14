package com.hpy.ops360.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemarksHimsDto extends GenericDto{
	
	@JsonIgnore
	private Long id;

	private String username;
	private String comment;
	private String date;
	private String owner;
	private String subcallType;
	public RemarksHimsDto(String username, String comment, String date, String owner, String subcallType) {
		this.username = username;
		this.comment = comment;
		this.date = date;
		this.owner = owner;
		this.subcallType = subcallType;
	}
	
	

}
