package com.hpy.mappingservice.request.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermanentCEBankNameFilterDto {
    
	@JsonIgnore
	private String id;
    private String bank_name;
    private String count;
	
    public PermanentCEBankNameFilterDto(String bank_name, String count) {
		super();
		this.bank_name = bank_name;
		this.count = count;
	}
    
    
}
