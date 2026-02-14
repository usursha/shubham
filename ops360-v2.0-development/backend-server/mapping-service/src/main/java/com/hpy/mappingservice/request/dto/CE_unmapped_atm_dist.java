package com.hpy.mappingservice.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CE_unmapped_atm_dist {

	private String ceUsername;
	private String newMappedCE;
	private String atmCodeList;
}
