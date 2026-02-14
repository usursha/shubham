package com.hpy.mappingservice.response.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistinctATMMetadataDTO extends GenericDto {
    @JsonIgnore
    private Long id;

    private List<BankCountDTO> bankCounts;
    private List<CityCountDTO> cityCounts;

    public DistinctATMMetadataDTO(List<BankCountDTO> bankCounts, List<CityCountDTO> cityCounts) {
        super();
        this.bankCounts = bankCounts;
        this.cityCounts = cityCounts;
    }
}

