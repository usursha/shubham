package com.hpy.ops360.report_service.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DowtimeDataDTO extends GenericDto{
	
	@JsonIgnore
	private Long id;

	private Integer totalCount;
	private List<UptimeReportResultDTO> data;
}
