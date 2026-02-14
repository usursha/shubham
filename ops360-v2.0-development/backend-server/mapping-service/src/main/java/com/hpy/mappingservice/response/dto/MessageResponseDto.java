package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDto extends GenericDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5797070335993195401L;
	@JsonIgnore
	private Long id;
	private String message;
	public MessageResponseDto(String message) {
		super();
		this.message = message;
	}
	

}
