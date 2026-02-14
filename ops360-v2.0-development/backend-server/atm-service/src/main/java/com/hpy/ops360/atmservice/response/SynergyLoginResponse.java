package com.hpy.ops360.atmservice.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SynergyLoginResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String requestid;
	private String status;

}
