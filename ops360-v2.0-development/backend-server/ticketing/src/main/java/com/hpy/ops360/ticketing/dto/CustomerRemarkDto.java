package com.hpy.ops360.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@EqualsAndHashCode(callSuper = true)
public class CustomerRemarkDto extends GenericDto{
	
	private static final long serialVersionUID = -3803211042631138328L;
	@JsonIgnore
	private Long id;
	private String remarks;
	public CustomerRemarkDto(String remarks) {
		super();
		this.remarks = remarks;
	}
	
	
}
