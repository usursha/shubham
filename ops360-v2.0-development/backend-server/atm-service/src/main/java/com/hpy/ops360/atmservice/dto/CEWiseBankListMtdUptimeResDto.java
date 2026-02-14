package com.hpy.ops360.atmservice.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CEWiseBankListMtdUptimeResDto {

	@JsonIgnore
	private Long id;
   private Long srNo;        
    private String bankName;  
    private BigDecimal mtdUptime; 
    private String dateTime; 
}
