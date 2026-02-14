package com.hpy.uam.dto;

import java.util.List;

import org.keycloak.representations.idm.ClientRepresentation;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientRepresentationListResponseDto extends GenericDto{
	
	private List<ClientRepresentation> clientRepresentation;
}
