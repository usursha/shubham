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
public class InActCeAtmMappedDto extends GenericDto {


	private static final long serialVersionUID = 1L;

	public InActCeAtmMappedDto(int srno, String message, String dataMessage) {
		super();
		this.srno = srno;
		this.message = message;
		this.dataMessage = dataMessage;
	}

	@JsonIgnore
	private Long id;
	private int srno;

	private String message;

	private String dataMessage;

}
