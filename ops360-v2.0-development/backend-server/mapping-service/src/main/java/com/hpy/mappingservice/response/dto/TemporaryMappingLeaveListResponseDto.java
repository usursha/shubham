package com.hpy.mappingservice.response.dto;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TemporaryMappingLeaveListResponseDto extends GenericDto{

	private String ceUserId;
	private Long atmCount;
	private String fullName;
	private String customStartTime;
	private String customEndTime;
	private String percentage;
}
