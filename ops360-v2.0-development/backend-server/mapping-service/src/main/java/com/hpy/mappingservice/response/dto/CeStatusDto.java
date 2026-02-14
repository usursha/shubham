package com.hpy.mappingservice.response.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CeStatusDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6216821569215816435L;
	private String username;
	private int status;

}
