package com.hpy.ops360.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class AtmUpDownCountDto  extends GenericDto{

	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private Long id;
//	private Long srNo;
	private int downAtm;
	private int upAtm;
	private int totalAtm;

}
