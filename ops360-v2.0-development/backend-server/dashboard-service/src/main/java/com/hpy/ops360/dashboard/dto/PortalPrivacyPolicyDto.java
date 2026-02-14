package com.hpy.ops360.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortalPrivacyPolicyDto extends GenericDto{
	@JsonIgnore
	private Long id;
	private Long srno;
	private String content;

}
