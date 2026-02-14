package com.hpy.ops360.atmservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class AtmMappingDto extends GenericDto {
	

	private static final long serialVersionUID = 1L;

	public AtmMappingDto(long srno, String ceUserId, String displayName, long atmCount) {
		super();
		this.srno = srno;
		this.ceUserId = ceUserId;
		this.displayName = displayName;
		this.atmCount = atmCount;
	}

	@JsonIgnore
	private Long id;
	
	private long srno;
	
	private String ceUserId;
	
	private String displayName;
	
	private long atmCount;

}
