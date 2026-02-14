package com.hpy.ops360.ticketing.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResponseMessage extends GenericDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private Long id;
	private String message;
	private int isClosed;
	public ResponseMessage(String message,int isClosed) {
		super();
		this.message = message;
		this.isClosed = isClosed;
	}
	
	

}
