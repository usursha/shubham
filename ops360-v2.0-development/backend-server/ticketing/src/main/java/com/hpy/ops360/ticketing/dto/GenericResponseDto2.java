package com.hpy.ops360.ticketing.dto;

import java.io.Serializable;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@SuperBuilder
public class GenericResponseDto2 extends GenericDto implements Serializable {


	private static final long serialVersionUID = 1L;

	    private String statusCode;
	    private String message;
	    private Object data; // Added a generic data field
	
}
