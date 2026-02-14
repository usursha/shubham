package com.hpy.mappingservice.response.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemporaryCeManualFilterResDto extends GenericDto {
	
	@JsonIgnore
	private Long id;
	
	private List <TemporaryCeManualBankFilterDto> bankList;
	private List <TemporaryCeManualCityFilterDto> cityList;
	
	public TemporaryCeManualFilterResDto(List<TemporaryCeManualBankFilterDto> bankList, List<TemporaryCeManualCityFilterDto> cityList ) {
		super();
		this.bankList = bankList;
		this.cityList = cityList;
	}


}
