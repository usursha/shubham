package com.hpy.ops360.atmservice.dto;

import java.util.List;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CEWiseBankListMtdUptimeResDto2 extends GenericDto{

    private List<CEWiseBankListMtdUptimeResDto> ceWiseBankList;
}
