package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManageUpcomingLeavesCeBankFilterResDto {

	@JsonIgnore
	private Long srno;
    private String bank_name;
    private int count;
	
    public ManageUpcomingLeavesCeBankFilterResDto(String bank_name, int count) {
		super();
		this.bank_name = bank_name;
		this.count = count;
	}
    
}
