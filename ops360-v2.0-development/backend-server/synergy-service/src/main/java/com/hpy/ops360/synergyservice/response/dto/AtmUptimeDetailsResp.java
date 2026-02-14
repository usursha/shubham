package com.hpy.ops360.synergyservice.response.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmUptimeDetailsResp implements Serializable {

	private String requestid;
	private String atmid;
	private double monthtotilldateuptime;
	private double lastmonthuptime;

}
