package com.hpy.ops360.ticketing.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemarkHistoryResponseDto {
    private String date;
    private List<RemarksDtoForCm> data;
    
    
}