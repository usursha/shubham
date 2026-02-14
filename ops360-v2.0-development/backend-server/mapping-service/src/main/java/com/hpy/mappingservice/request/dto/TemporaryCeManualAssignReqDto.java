package com.hpy.mappingservice.request.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemporaryCeManualAssignReqDto {
    private Long leaveId;
    private String mappedType;
    private List<TemporaryCeManualAssignEntryReqDto> mappings;
}
