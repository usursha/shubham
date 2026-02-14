package com.hpy.ops360.atmservice.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMWiseBankListMtdUptimeResDto extends GenericDto{
	
		@JsonIgnore
		private Long id;
	   private Long srNo;        
	    private String bankName;  
	    private BigDecimal mtdUptime; 
	    private String dateTime;  
	

}