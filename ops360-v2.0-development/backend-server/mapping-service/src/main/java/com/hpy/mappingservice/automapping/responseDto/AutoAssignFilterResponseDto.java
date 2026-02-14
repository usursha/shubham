package com.hpy.mappingservice.automapping.responseDto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AutoAssignFilterResponseDto extends GenericDto{

	@JsonIgnore
	private Long id;
	private List<BankInfo> bank;
    private List<CityInfo> city;

}
