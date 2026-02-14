package com.hpy.mappingservice.response.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;
import com.hpy.mappingservice.request.dto.PermanentCEBankNameFilterDto;
import com.hpy.mappingservice.request.dto.PermanentCECityDataFilterDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermanentCEFilterResponseDto extends GenericDto {

	@JsonIgnore
	private Long id;
	private List<PermanentCEBankNameFilterDto> bankList;
	private List<PermanentCECityDataFilterDto> cityList;
	
	public PermanentCEFilterResponseDto(List<PermanentCEBankNameFilterDto> bankList,
			List<PermanentCECityDataFilterDto> cityList) {
		super();
		this.bankList = bankList;
		this.cityList = cityList;
	}
	
	

}

