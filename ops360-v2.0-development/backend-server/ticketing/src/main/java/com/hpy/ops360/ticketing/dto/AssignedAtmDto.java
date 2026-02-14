package com.hpy.ops360.ticketing.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class AssignedAtmDto implements Serializable {

	private static final long serialVersionUID = -7111696076928975858L;
	private String atmId;
	private String bank;
	private String location;

}
