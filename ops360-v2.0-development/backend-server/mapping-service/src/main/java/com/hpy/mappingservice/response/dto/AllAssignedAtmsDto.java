package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllAssignedAtmsDto extends GenericDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private Long id;
	
	private Long srno;
	
	private String atmIds;

	public AllAssignedAtmsDto(Long srno, String atmIds) {
		super();
		this.srno = srno;
		this.atmIds = atmIds;
	}
	
	

}
