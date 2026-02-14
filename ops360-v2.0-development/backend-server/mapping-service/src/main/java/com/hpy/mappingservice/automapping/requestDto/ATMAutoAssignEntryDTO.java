package com.hpy.mappingservice.automapping.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ATMAutoAssignEntryDTO {
    private String ceIdList; //comma-separated ceIdList
    private Long tempMappedCeId;
    private String atmList; // comma-separated ATM codes
}