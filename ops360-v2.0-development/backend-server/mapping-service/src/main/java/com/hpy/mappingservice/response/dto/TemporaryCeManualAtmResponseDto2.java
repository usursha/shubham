package com.hpy.mappingservice.response.dto;

import java.util.List;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemporaryCeManualAtmResponseDto2 extends GenericDto {
    private List<TemporaryCeManualAtmResponseDto> ceMappingDetailsList;

}
