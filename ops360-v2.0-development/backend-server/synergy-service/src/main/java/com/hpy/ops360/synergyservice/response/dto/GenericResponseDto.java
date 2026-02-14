package com.hpy.ops360.synergyservice.response.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenericResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String statusCode;
	private String message;
}
