package com.hpy.mappingservice.response.dto;

import java.util.List;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManageUpcomingLeavesCeAtmFilterResDto extends GenericDto {
	
	private Long id;
	
	private List <ManageUpcomingLeavesCeBankFilterResDto> bankList;
	private List <ManageUpcomingLeavesCeCityFilterResDto> cityList;
	
	public ManageUpcomingLeavesCeAtmFilterResDto(List<ManageUpcomingLeavesCeBankFilterResDto> bankList, List<ManageUpcomingLeavesCeCityFilterResDto> cityList ) {
		super();
		this.bankList = bankList;
		this.cityList = cityList;
	}


}
