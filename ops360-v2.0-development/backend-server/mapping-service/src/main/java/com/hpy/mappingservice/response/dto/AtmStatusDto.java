package com.hpy.mappingservice.response.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmStatusDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3655420721766964153L;
	private String atmId;
	private int status;

}
