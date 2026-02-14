package com.hpy.mappingservice.response.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@EqualsAndHashCode(callSuper = true)
public class AllCeListDto extends GenericDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private Long id;
	
	private Long srno;
	
	private String username;
	
	private String displayName;
	
	private String userStatus;

	public AllCeListDto(Long srno, String username, String displayName, String userStatus) {
		super();
		this.srno = srno;
		this.username = username;
		this.displayName = displayName;
		this.userStatus = userStatus;
	}
	
	
	
}
