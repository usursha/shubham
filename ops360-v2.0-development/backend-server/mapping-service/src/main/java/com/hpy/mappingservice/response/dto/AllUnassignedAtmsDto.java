package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllUnassignedAtmsDto extends GenericDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6106440758074855596L;

	@JsonIgnore
	private Long id;
	
	private Long srno;

	private String atmId;

	public AllUnassignedAtmsDto(Long srno, String atmId) {
		super();
		this.srno = srno;
		this.atmId = atmId;
	}
	
	

}
