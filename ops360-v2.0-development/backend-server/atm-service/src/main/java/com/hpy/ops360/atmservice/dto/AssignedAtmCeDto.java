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
public class AssignedAtmCeDto extends GenericDto {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	private Long id;

	public AssignedAtmCeDto(long srno, String atmId, String bankName, String siteName) {
		super();
		this.srno = srno;
		this.atmId = atmId;
		this.bankName = bankName;
		this.siteName = siteName;
	}

	private long srno;
	private String atmId;
	private String bankName;
	private String siteName;

}
