package com.hpy.ops360.atmservice.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SynergyResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4066214718041477593L;
	private String requestid;
	private String status;

}
