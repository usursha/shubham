package com.hpy.ops360.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FlagStatusDto {

	private String ceUserId;

	private int flagStatus;
}
