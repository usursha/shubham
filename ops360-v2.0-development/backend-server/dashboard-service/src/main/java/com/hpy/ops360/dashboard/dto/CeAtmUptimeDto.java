package com.hpy.ops360.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class CeAtmUptimeDto extends GenericDto {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private Long id;

	private String lastUpdated;
	private String uptime;
	private Integer atmCount;
	private String remarks;

	@Builder
	public CeAtmUptimeDto(String lastUpdated, String uptime, Integer atmCount, String remarks) {
		this.lastUpdated = lastUpdated;
		this.uptime = uptime;
		this.atmCount = atmCount;
		this.remarks = remarks;
	}

}
