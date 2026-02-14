package com.hpy.mappingservice.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermanentCeAtmListMappingDto {

	 private Long primaryCeId;
	 private Long mappedCeId;
	 private String ce_name;
	 private String atms;
}
