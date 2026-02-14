package com.hpy.ops360.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IsTravellingDto extends GenericDto {

	@JsonIgnore
	private Long id;
	private static final long serialVersionUID = 1L;
	private Integer isTravelling;
	public IsTravellingDto(Integer isTravelling) {
		super();
		this.isTravelling = isTravelling;
	}
	

}
