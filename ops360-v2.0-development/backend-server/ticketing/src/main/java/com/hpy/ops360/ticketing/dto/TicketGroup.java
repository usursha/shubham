package com.hpy.ops360.ticketing.dto;

import java.util.List;

import com.hpy.ops360.ticketing.cm.dto.AtmShortDetailsDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketGroup {
    private String date;
    private List<AtmShortDetailsDto> data;
}
