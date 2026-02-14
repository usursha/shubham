package com.hpy.ops360.dashboard.dto;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmStatusDtoForCm extends GenericDto{

	private int upAtmCount;
	private int downAtmCount;
	private int totalAtmCount;
}
