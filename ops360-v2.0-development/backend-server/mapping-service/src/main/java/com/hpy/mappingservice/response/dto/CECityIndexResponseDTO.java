package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CECityIndexResponseDTO extends GenericDto{
	   
	   @JsonIgnore
	   private Long id;
	   private Long srNo;
	   private String city;

}
