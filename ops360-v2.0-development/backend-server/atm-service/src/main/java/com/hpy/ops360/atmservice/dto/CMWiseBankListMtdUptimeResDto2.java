package com.hpy.ops360.atmservice.dto;

import java.math.BigDecimal;
import java.util.List;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMWiseBankListMtdUptimeResDto2 extends GenericDto{
	
//    private BigDecimal overallMtdUptime;
    private List<CMWiseBankListMtdUptimeResDto> cmWiseBankList;	

}