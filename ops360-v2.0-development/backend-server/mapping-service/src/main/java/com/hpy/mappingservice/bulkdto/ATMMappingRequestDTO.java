package com.hpy.mappingservice.bulkdto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ATMMappingRequestDTO {
    private Long leaveId;
    private String mappedType;
    private List<ATMMappingEntryDTO> mappings;
}
