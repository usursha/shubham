package com.hpy.ops360.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CMPortalDashboardDto_AtmStatusDto extends GenericDto {
	@JsonIgnore
    private Long id;

	private AtmStatusDto atmStatusDto;
}
