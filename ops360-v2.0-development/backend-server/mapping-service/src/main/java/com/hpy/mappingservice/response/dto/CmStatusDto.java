package com.hpy.mappingservice.response.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmStatusDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7774366816822675157L;
	private String username;
	private int status;

}
