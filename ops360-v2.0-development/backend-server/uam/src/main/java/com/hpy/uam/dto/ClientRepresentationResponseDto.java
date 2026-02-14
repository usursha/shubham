package com.hpy.uam.dto;

import org.keycloak.representations.idm.ClientRepresentation;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientRepresentationResponseDto extends GenericDto{
	
	
	private ClientRepresentation clientRepresentation;

}
