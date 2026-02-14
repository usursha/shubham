package com.hpy.mappingservice.request.dto;

import com.hpy.mappingservice.bulkdto.ATMMappingEntryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecondaryATMMappingRequestDTO {
    private Long leaveId;
    private String mappedType;
    private ATMMappingEntryDTO mappings;
}
