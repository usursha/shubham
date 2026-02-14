package com.hpy.ops360.ticketing.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponseDto extends GenericDto implements Serializable {


	private static final long serialVersionUID = 1L;
	@Builder
	public GenericResponseDto(String statusCode, String message) {
		this.statusCode=statusCode;
		this.message= message;
	}
	@JsonIgnore
	private Long id;
	private String statusCode;
	private String message;
}
