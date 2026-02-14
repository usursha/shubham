package com.hpy.ops360.ticketing.response;

import java.io.Serializable;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmUptimeDetailsResp extends GenericDto implements Serializable {

	private String requestid;
	private String atmid;
	private double monthtotilldateuptime;
	private double lastmonthuptime;

}
