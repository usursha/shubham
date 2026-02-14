package com.hpy.mappingservice.request.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ATMResettingRequestDTO {
    private String leaveId;
    private String reason;
    private String reasoncomment;
}
