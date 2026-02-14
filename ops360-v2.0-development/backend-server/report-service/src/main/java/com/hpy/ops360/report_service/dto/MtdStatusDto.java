package com.hpy.ops360.report_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MtdStatusDto extends GenericDto {
	
	@JsonIgnore
	private Long id;
	
    private String lastTxnUpdatedDate;
    private String mtdUptimeUpdatedDate;
    
}
