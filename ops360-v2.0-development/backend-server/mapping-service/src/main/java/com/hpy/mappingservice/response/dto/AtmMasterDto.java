package com.hpy.mappingservice.response.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AtmMasterDto extends GenericDto{
	 
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonIgnore
	 private Long id;
	 private Long atmId;
	 private String atmCode;
	 private String bankName;
	 private String grade;
	 private String city;
	 private String state;
	 private String address;
	 private String zone;
	 private String source;
	 
	public AtmMasterDto(Long atmId, String atmCode, String bankName, String grade, String city, String state,
			String address, String zone,String source) {
		super();
		this.atmId = atmId;
		this.atmCode = atmCode;
		this.bankName = bankName;
		this.grade = grade;
		this.city = city;
		this.state = state;
		this.address = address;
		this.zone=zone;
		this.source=source;
	}
	 
	 
	 
	 
}
