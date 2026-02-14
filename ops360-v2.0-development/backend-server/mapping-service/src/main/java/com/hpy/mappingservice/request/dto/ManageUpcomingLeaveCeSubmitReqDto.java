package com.hpy.mappingservice.request.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManageUpcomingLeaveCeSubmitReqDto {
    private Long leaveId;
    private List<ManageUpcomingLeaveCeSubmitEntryReqDto> mappings;
}

