package com.hpy.mappingservice.response.dto;
import java.util.List;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CEIndexWrapperDto extends GenericDto{
	
	
	private Integer totalCount;
  //  private String sampatiTxnDate;
    private List<IndexOfCeResponseDto> record;

	    
	
}
