package com.hpy.ops360.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmDetailsWithSourceDto {
	private String atmid;
	private String source;
}
