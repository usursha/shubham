package com.hpy.ops360.dashboard.dto;

import lombok.Data;

@Data
public class AtmStatusDto {
	private Integer upAtmCount;
	private Integer downAtmCount;
	private String remarks;

}
