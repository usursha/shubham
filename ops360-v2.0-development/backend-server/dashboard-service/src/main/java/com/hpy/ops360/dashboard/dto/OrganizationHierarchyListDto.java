package com.hpy.ops360.dashboard.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.dashboard.entity.OrganizationHierarchy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationHierarchyListDto extends GenericDto {
	
	@JsonIgnore
	private Long id;
	
	private PortalPersonalDetailsDto personalDetails;
	
	private PortalWorkMetricsDto workDetails;
	
	private List<OrganizationHierarchy> organizationHierarchy;
	

}
