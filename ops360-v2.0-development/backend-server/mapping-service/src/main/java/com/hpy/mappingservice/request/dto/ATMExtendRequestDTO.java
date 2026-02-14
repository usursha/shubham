package com.hpy.mappingservice.request.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ATMExtendRequestDTO {
    private Long leaveId;
    
    private LocalDateTime extendDate;
}
