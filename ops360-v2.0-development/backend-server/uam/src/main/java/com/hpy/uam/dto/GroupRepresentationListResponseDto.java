package com.hpy.uam.dto;

import java.util.List;

import org.keycloak.representations.idm.GroupRepresentation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupRepresentationListResponseDto extends GenericDto{
	
	@JsonIgnore
    private Long id;
	
	private List<GroupRepresentation> list; 

}
