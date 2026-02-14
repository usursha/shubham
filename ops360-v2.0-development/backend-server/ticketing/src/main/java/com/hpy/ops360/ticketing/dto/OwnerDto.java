package com.hpy.ops360.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
//@EqualsAndHashCode(callSuper = true)
public class OwnerDto extends GenericDto {
	private static final long serialVersionUID = 3545462050272926209L;

	@JsonIgnore
	private Long id;
	private String name;
	
	private String etaEnable;
	
	private String updateEnable;

	public OwnerDto(String name, String etaEnable,String updateEnable) {
		super();
		this.name = name;
		this.etaEnable = etaEnable;
		this.updateEnable=updateEnable;
	}
	
	

}
