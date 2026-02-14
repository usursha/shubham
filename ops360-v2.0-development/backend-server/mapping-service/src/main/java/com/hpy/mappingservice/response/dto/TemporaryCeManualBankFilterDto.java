package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.mappingservice.request.dto.PermanentCEBankNameFilterDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

	
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemporaryCeManualBankFilterDto {
    
	@JsonIgnore
	private String id;
    private String bank_name;
    private String count;
	
    public TemporaryCeManualBankFilterDto(String bank_name, String count) {
		super();
		this.bank_name = bank_name;
		this.count = count;
	}
    
    
}
