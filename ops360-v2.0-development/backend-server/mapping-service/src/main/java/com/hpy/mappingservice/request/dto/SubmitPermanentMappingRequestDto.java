package com.hpy.mappingservice.request.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitPermanentMappingRequestDto {

	 private Long leave_id;
	 private String mappedType;
	 private List<PermanentCeAtmListMappingDto> mapping_list;
}
