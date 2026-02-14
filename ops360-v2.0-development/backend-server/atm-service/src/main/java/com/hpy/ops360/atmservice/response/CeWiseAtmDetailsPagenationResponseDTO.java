package com.hpy.ops360.atmservice.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.atmservice.dto.CeWiseAtmDataDetailsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class CeWiseAtmDetailsPagenationResponseDTO extends GenericDto {

	@JsonIgnore
	private Long id;
	private static final long serialVersionUID = 1L;
    private int totalRecords;
    private List<CeWiseAtmDataDetailsDto> record;

	
}
