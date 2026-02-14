package com.hpy.mappingservice.request.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemporaryCeManualAssignEntryReqDto {
    private String atmCode; // Single ATM code
    private Long ceId;     // Primary CE ID
    private Long tempMappedCeId; // Temporary CE ID
}
