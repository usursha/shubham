package com.hpy.ops360.dashboard.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SynergyLoginResponse implements Serializable {

	private String requestid;
	private String status;

}
