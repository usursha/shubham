package com.hpy.ops360.atmservice.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssignedAtmDto extends GenericDto implements Serializable {

	@JsonIgnore
	private Long id;
	private static final long serialVersionUID = -7111696076928975858L;
	private String atmId;
	private String bank;
	private String location;
	private String atmCategory;
	public AssignedAtmDto(String atmId, String bank, String location, String atmCategory) {
		super();
		this.atmId = atmId;
		this.bank = bank;
		this.location = location;
		this.atmCategory = atmCategory;
	}
	
	

}
