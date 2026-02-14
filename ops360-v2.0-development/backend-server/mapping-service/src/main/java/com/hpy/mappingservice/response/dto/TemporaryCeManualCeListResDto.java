package com.hpy.mappingservice.response.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemporaryCeManualCeListResDto extends GenericDto {
    @JsonIgnore
    private Long id;
    private List<TemporaryCeManualCeListDto> data;

    public TemporaryCeManualCeListResDto(List<TemporaryCeManualCeListDto> data) {
        super();
        this.data = data;
    }
}
