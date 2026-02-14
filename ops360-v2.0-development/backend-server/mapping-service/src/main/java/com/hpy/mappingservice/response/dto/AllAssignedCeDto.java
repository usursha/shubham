package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllAssignedCeDto extends GenericDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6106440758074855596L;

	@JsonIgnore
	private Long id;
	
	private Long srno;

	private String username;
	
	private String displayName;

	public AllAssignedCeDto(Long srno,String username, String displayName) {
		super();
		this.srno = srno;
		this.username = username;
		this.displayName=displayName;
	}
	
	

}
