package com.hpy.ops360.atmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SubCallTypeOption {
	private int subCallTypeId;
	private String subCallTypeName;
	private int subCallTypeCount;
}