package com.hpy.ops360.dashboard.dto;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class NumberOfCeDto  extends GenericDto{


	private static final long serialVersionUID = 8734450530383346186L;

	private String totalNoCe;

	private String activeCe;

	private String inactiveCe;
}
